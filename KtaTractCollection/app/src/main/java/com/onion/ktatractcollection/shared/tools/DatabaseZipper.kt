package com.onion.ktatractcollection.shared.tools

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.onion.ktatractcollection.Models.MimeType
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.jsonToObject
import com.onion.ktatractcollection.shared.extensions.toJson
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class DatabaseZipper(private val context: Context) {

    /**
     * Properties
     */

    private val filesDir = context.applicationContext.filesDir

    /**
     * Methods | Export
     */

    fun export(destination: Uri, tracts: List<Tract>, pictures: List<TractPicture>) {
        Log.i(TAG, "Export tract and pictures collections")

        export(tracts, TRACT_LIST_JSON_FILENAME)
        export(pictures, PICTURE_LIST_JSON_FILENAME)

        val files = arrayOf(
            TRACT_LIST_JSON_FILENAME,
            PICTURE_LIST_JSON_FILENAME
        ) + pictures.map { it.photoFilename.toUri().lastPathSegment ?: "" }

        val documentFile = DocumentFile.fromTreeUri(context, destination)
        val destinationFile =
            documentFile?.createFile(MimeType.ZIP.string, ZIP_FILENAME)

        destinationFile?.let { file ->
            Zipper(context).zip(file.uri, files.map { File(filesDir, it) }.toTypedArray())
        }
    }

    private fun export(objectToExport: Any, filename: String) {
        val file = File(filesDir, filename)
        val result = objectToExport.toJson()

        file.writeText(result)
    }

    /**
     * Methods | Import
     */

    fun unzipFile(uri: Uri) {
        Unzipper(context).unzip(uri)
    }

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
     * Companion
     */

    companion object {

        private const val TRACT_LIST_JSON_FILENAME = "tractList.json"
        private const val PICTURE_LIST_JSON_FILENAME = "pictureList.json"
        private const val ZIP_FILENAME = "export.zip"

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