package com.unicorpdev.ktatract.shared.tools.zip

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Zipper(val context: Context) {

    /**
     * Properties
     */
    private val contentResolver = context.contentResolver

    /**
     * Methods
     */

    fun zip(outputUri: Uri, files: Array<File>): Boolean {
        return try {
            val destination = contentResolver.openOutputStream(outputUri)
            val outputZip = ZipOutputStream(BufferedOutputStream(destination))

            files.forEach { addFileToZip(it, outputZip) }

            outputZip.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Extract from https://androidpedia.net/en/tutorial/8137/zip-file-in-android
    private fun addFileToZip(file: File, zip: ZipOutputStream) {
        if (!file.exists()) { return }

        Log.v(TAG, "Adding: ${file.path} to zip")

        val inputStream = FileInputStream(file)
        val entry = ZipEntry(file.toUri().lastPathSegment)

        zip.putNextEntry(entry)

        writeInputStreamToZip(inputStream, zip)
    }

    private fun writeInputStreamToZip(inputStream: FileInputStream, zip: ZipOutputStream) {
        val origin = BufferedInputStream(inputStream, BUFFER)
        val data = ByteArray(BUFFER)
        var count: Int

        while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
            zip.write(data, 0, count)
        }

        origin.close()
    }

    /**
     * Companion
     */

    companion object {
        private val TAG = Zipper::class.simpleName ?: "Default"

        private const val BUFFER = 2048
    }
}