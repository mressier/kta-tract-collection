package com.onion.ktatractcollection.shared.tools

import android.content.Context
import android.util.Log
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.jsonToObject
import com.onion.ktatractcollection.shared.extensions.toJson
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class DatabaseZipper(context: Context) {

    /**
     * Properties
     */

    private val filesDir = context.applicationContext.filesDir

    /**
     * Methods | Export
     */

    fun export(tracts: List<Tract>, pictures: List<TractPicture>) {
        Log.i(TAG, "Export tract and pictures collections")

        export(tracts, TRACT_LIST_JSON_FILENAME)
        export(pictures, PICTURE_LIST_JSON_FILENAME)

        val files = arrayOf(
            TRACT_LIST_JSON_FILENAME,
            PICTURE_LIST_JSON_FILENAME
        ) + pictures.map { it.photoFilename }

        zip(ZIP_FILENAME, files)
    }

    private fun export(objectToExport: Any, filename: String) {
        val file = File(filesDir, filename)
        val result = objectToExport.toJson()

        file.writeText(result)
    }

    /**
     * Methods | Import
     */

    fun importTracts(): List<Tract>? {
        return importObject<List<Tract>>(TRACT_LIST_JSON_FILENAME)
    }

    fun importPictures(): List<TractPicture>? {
        return importObject<List<TractPicture>>(PICTURE_LIST_JSON_FILENAME)
    }

    private inline fun <reified T> importObject(filename: String): T? {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            Log.i(TAG, "Nothing to import from $filename. File doesn't exist")
            return null
        }

        val result = file.readText()
        return result.jsonToObject()
    }

    /**
     * Methods | Zip export
     */

    fun zip(filename: String, files: Array<String>) {
        try {
            val destinationFile = FileOutputStream(File(filesDir, filename))
            val outputZip = ZipOutputStream(BufferedOutputStream(destinationFile))

            files.forEach { filename ->
                addFileToZip(filename, outputZip)
            }

            outputZip.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addFileToZip(filename: String, zip: ZipOutputStream) {
        val file = File(filesDir, filename)

        Log.v("Compress", "Adding: ${file.path}")

        if (!file.exists()) { return }

        addFileToZip(file, zip)
    }

    // Extract from https://androidpedia.net/en/tutorial/8137/zip-file-in-android
    private fun addFileToZip(file: File, zip: ZipOutputStream) {

        val data = ByteArray(BUFFER)
        val fi = FileInputStream(file)
        val origin = BufferedInputStream(fi, BUFFER)

        val entry = ZipEntry(file.path.substring(file.path.lastIndexOf("/") + 1))
        zip.putNextEntry(entry)

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

        private const val TRACT_LIST_JSON_FILENAME = "tractList.json"
        private const val PICTURE_LIST_JSON_FILENAME = "pictureList.json"
        private const val ZIP_FILENAME = "export.zip"
        private const val BUFFER = 2048

        private val TAG = DatabaseZipper::class.simpleName ?: "Default"

        private var INSTANCE: DatabaseZipper? = null

        fun initialize(context: Context) {
            INSTANCE = DatabaseZipper(context)
        }

        fun get(): DatabaseZipper {
            return INSTANCE ?: throw IllegalStateException("TractExporter must be initialized")
        }
     }

}