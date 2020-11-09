package com.onion.ktatractcollection.shared.tools

import android.content.Context
import android.net.Uri
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
            val fileOutput = File(filesDir, zipEntry!!.name)
            val outputStream = FileOutputStream(fileOutput)

            val buffer = ByteArray(1024)
            var length: Int
            while (zipInput.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            zipInput.closeEntry()
            outputStream.close()
        }

        zipInput.close()
    }

    /**
     * Companion
     */

    companion object {
        private val TAG = Unzipper::class.simpleName ?: "Default"
     }
}