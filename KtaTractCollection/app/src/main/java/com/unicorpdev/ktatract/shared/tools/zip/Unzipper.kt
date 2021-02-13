package com.unicorpdev.ktatract.shared.tools.zip

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class Unzipper(context: Context) {

    /**
     * Properties
     */

    private val contentResolver = context.contentResolver

    /**
     * Methods
     */

    fun unzip(zipUri: Uri, outputDir: File) {
        val inputStream = contentResolver.openInputStream(zipUri)

        val zipInput = ZipInputStream(inputStream)

        var zipEntry: ZipEntry?
        while (zipInput.nextEntry.also { zipEntry = it } != null) {
            zipEntry?.let { unzipFileEntry(zipInput, it, outputDir) }
        }

        zipInput.close()
    }

    fun containsFiles(zipUri: Uri, files: Array<String>): Boolean {
        val filenames = getFilesInZip(zipUri)
        val missingFiles = files.filter { !filenames.contains(it) }

        return missingFiles.isEmpty()
    }

    // Return the list of files from input file list that are not present inside the zip
    fun filesNotContainedInZip(zipUri: Uri, files: Array<String>): Array<String> {
        val filenames = getFilesInZip(zipUri)
        val missingFiles = files.filter { !filenames.contains(it) }

        return missingFiles.toTypedArray()
    }

    private fun unzipFileEntry(zip: ZipInputStream, entry: ZipEntry, outputDir: File) {
        Log.v(TAG, "Reading file ${entry.name} from zip")

        val fileOutput = File(outputDir, entry.name)
        val outputStream = FileOutputStream(fileOutput)

        var length: Int
        while (zip.read(BUFFER).also { length = it } > 0) {
            outputStream.write(BUFFER, 0, length)
        }
        zip.closeEntry()
        outputStream.close()
    }

    private fun getFilesInZip(zipUri: Uri): Array<String> {
        val filenames = mutableListOf<String>()

        val inputStream = contentResolver.openInputStream(zipUri)
        val zipInput = ZipInputStream(inputStream)
        var zipEntry: ZipEntry?

        while (zipInput.nextEntry.also { zipEntry = it } != null) {
            zipEntry?.name?.let { filenames.add(it) }
        }

        zipInput.close()

        return filenames.toTypedArray()
    }

    /**
     * Companion
     */

    companion object {

        private val TAG = Unzipper::class.simpleName ?: "Default"

        private val BUFFER = ByteArray(1024)
     }
}