package com.unicorpdev.ktatract.shared.tools.collection

import android.content.Context
import android.net.Uri
import android.util.Log
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.extensions.jsonToObject
import com.unicorpdev.ktatract.shared.tools.JsonFile
import com.unicorpdev.ktatract.shared.tools.zip.Unzipper
import java.io.File
import java.io.FileNotFoundException
import java.io.Serializable
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Import database from a zip file
 *
 * Use a zip file (previously created by the [CollectionExporter]) to import a collection content
 */
class CollectionImporter(val context: Context) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val filesDir = TractRepository.get().filesDir
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
    @Throws(FileNotFoundException::class)
    fun unzipFile(zipUri: Uri, zipContentDestination: File, requiredFiles: Array<String>) {
        val missingFiles = unzipper.filesNotContainedInZip(zipUri, requiredFiles)

        if (missingFiles.isEmpty()) {
            Log.v(TAG, "Unzip file")
            unzipper.unzip(zipUri, zipContentDestination)
        } else {
            Log.e(TAG, "Missing files")
            throw FileNotFoundException(missingFiles.joinToString())
        }
    }

    fun getTractsFromJson(jsonFilename: String = CollectionFiles.TRACT_LIST_JSON_FILENAME): List<Tract>? {
        return JsonFile(filesDir, jsonFilename).getObject<List<Tract>>()
    }

    fun getPicturesFromJson(
        jsonFilename: String = CollectionFiles.PICTURE_LIST_JSON_FILENAME
    ): List<TractPicture>? {
        return JsonFile(filesDir, jsonFilename).getObject<List<TractPicture>>()
    }

    fun getCollectionsFromJson(
        jsonFilename: String = CollectionFiles.COLLECTION_LIST_JSON_FILENAME
    ): List<TractCollection>? {
        return JsonFile(filesDir, jsonFilename).getObject<List<TractCollection>>()
    }

    /**
     * Companion
     */

    companion object {
        private val TAG = CollectionImporter::class.simpleName ?: "Default"
    }
}