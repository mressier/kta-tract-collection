package com.onion.ktatractcollection.shared.tools.database

import android.content.Context
import android.net.Uri
import android.util.Log
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.jsonToObject
import com.onion.ktatractcollection.shared.tools.zip.Unzipper
import java.io.File

class DatabaseImporter(val context: Context) {

    /**
     * Properties
     */

    private val filesDir = context.applicationContext.filesDir


    /**
     * Methods | Import
     */

    fun unzipFile(uri: Uri) {
        Unzipper(context).unzip(uri)
    }

    fun importTracts(): List<Tract>? {
        return importObject<List<Tract>>(DatabaseFiles.TRACT_LIST_JSON_FILENAME)
    }

    fun importPictures(): List<TractPicture>? {
        return importObject<List<TractPicture>>(DatabaseFiles.PICTURE_LIST_JSON_FILENAME)
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
        private val TAG = DatabaseImporter::class.simpleName ?: "Default"
    }
}