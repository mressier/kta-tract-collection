package com.onion.ktatractcollection.shared.tools.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.onion.ktatractcollection.Models.MimeType
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.toJson
import com.onion.ktatractcollection.shared.tools.zip.Zipper
import java.io.*

class DatabaseExporter(private val context: Context) {

    /**
     * Properties
     */

    private val filesDir = context.applicationContext.filesDir

    /**
     * Methods | Export
     */

    fun export(destination: Uri, tracts: List<Tract>, pictures: List<TractPicture>) {
        Log.i(TAG, "Export tract and pictures collections")

        export(tracts, DatabaseFiles.TRACT_LIST_JSON_FILENAME)
        export(pictures, DatabaseFiles.PICTURE_LIST_JSON_FILENAME)

        val files = arrayOf(
            DatabaseFiles.TRACT_LIST_JSON_FILENAME,
            DatabaseFiles.PICTURE_LIST_JSON_FILENAME
        ) + pictures.map { it.photoFilename.toUri().lastPathSegment ?: "" }

        val documentFile = DocumentFile.fromTreeUri(context, destination)
        val destinationFile =
            documentFile?.createFile(MimeType.ZIP.string, DatabaseFiles.ZIP_FILENAME)

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
     * Companion
     */

    companion object {
        private val TAG = DatabaseExporter::class.simpleName ?: "Default"
     }

}