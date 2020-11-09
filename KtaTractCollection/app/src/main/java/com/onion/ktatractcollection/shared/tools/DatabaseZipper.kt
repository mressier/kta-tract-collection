package com.onion.ktatractcollection.shared.tools

import android.content.Context
import android.util.Log
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.jsonToObject
import com.onion.ktatractcollection.shared.extensions.toJson
import java.io.File
import java.lang.IllegalStateException

class TractExporter(context: Context) {

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
     * Companion
     */

    companion object {

        private const val TRACT_LIST_JSON_FILENAME = "tractList.json"
        private const val PICTURE_LIST_JSON_FILENAME = "pictureList.json"

        private val TAG = TractExporter::class.simpleName ?: "Default"

        private var INSTANCE: TractExporter? = null

        fun initialize(context: Context) {
            INSTANCE = TractExporter(context)
        }

        fun get(): TractExporter {
            return INSTANCE ?: throw IllegalStateException("TractExporter must be initialized")
        }
     }

}