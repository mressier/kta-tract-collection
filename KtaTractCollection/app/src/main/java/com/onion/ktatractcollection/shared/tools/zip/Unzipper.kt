package com.onion.ktatractcollection.shared.tools.zip

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
    private val filesDir = context.applicationContext.filesDir

    /**
     * Methods
     */

    fun unzip(file: Uri) {
        val inputStream = contentResolver.openInputStream(file)
        val zipInput = ZipInputStream(inputStream)

        var zipEntry: ZipEntry?
        while (zipInput.nextEntry.also { zipEntry = it } != null) {
            zipEntry?.let { unzipFileEntry(zipInput, it) }
        }

        zipInput.close()
    }

    private fun unzipFileEntry(zip: ZipInputStream, entry: ZipEntry) {
        Log.v(TAG, "Reading file ${entry.name} from zip")

        val fileOutput = File(filesDir, entry.name)
        val outputStream = FileOutputStream(fileOutput)

        var length: Int
        while (zip.read(BUFFER).also { length = it } > 0) {
            outputStream.write(BUFFER, 0, length)
        }
        zip.closeEntry()
        outputStream.close()
    }

    /**
     * Companion
     */

    companion object {

        private val TAG = Unzipper::class.simpleName ?: "Default"

        private val BUFFER = ByteArray(1024)
     }
}