package com.onion.ktatractcollection.shared.tools.collection

import android.content.Context
import android.net.Uri
import android.util.Log
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.extensions.jsonToObject
import com.onion.ktatractcollection.shared.tools.zip.Unzipper
import java.io.File
import java.io.FileNotFoundException

/**
 * Import database from a zip file
 *
 * Use a zip file (previously created by the [CollectionExporter]) to import a collection content
 */
class CollectionImporter(val context: Context) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val filesDir = context.applicationContext.filesDir
    private val unzipper = Unzipper(context)

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    /**
     * Unzip a zip file which is supposed to be a database container. Check that it contains
     * expected content before unzip it into the app private file directory
     *
     * @property zipUri the uri of the zip file
     * @throws FileNotFoundException if some files are missing inside the zip,
     *          so the database won't be able to be recreated
     */
    fun unzipFile(zipUri: Uri) {
        if (unzipper.containsFiles(zipUri, CollectionFiles.REQUIRED_FILES_IN_ZIP)) {
            Log.v(TAG, "Unzip file")
            unzipper.unzip(zipUri, filesDir)
        } else {
            Log.e(TAG, "Missing files")
            throw FileNotFoundException(CollectionFiles.REQUIRED_FILES_IN_ZIP.joinToString())
        }
    }

    /**
     * Import tract list from a json file
     *
     * @property jsonFilename name of the file that contains the tract collection.
     * @throws FileNotFoundException if the json file doesn't exist
     * @return list of tracts imported from the file
     */
    fun importTracts(jsonFilename: String = CollectionFiles.TRACT_LIST_JSON_FILENAME): List<Tract>? {
        return importObject<List<Tract>>(jsonFilename)
    }

    /**
     * Import picture list from a json file
     *
     * @property jsonFilename name of the file that contains the picture collection
     * @throws FileNotFoundException if the json file doesn't exist
     * @return list of pictures imported from the file
     */
    fun importPictures(
        jsonFilename: String = CollectionFiles.PICTURE_LIST_JSON_FILENAME
    ): List<TractPicture>? {
        return importObject<List<TractPicture>>(jsonFilename)
    }
    
    /***********************************************************************************************
     * Tools
     **********************************************************************************************/

    private inline fun <reified T> importObject(filename: String): T? {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            throw FileNotFoundException(filename)
        }

        val result = file.readText()
        return result.jsonToObject()
    }

    /**
     * Companion
     */

    companion object {
        private val TAG = CollectionImporter::class.simpleName ?: "Default"
    }
}