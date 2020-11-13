package com.unicorpdev.ktatract.shared.tools.collection

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.unicorpdev.ktatract.models.MimeType
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.extensions.toJson
import com.unicorpdev.ktatract.shared.tools.zip.Zipper
import java.io.*

/**
 * Export a collection of tracts and pictures to a zip file
 */
class CollectionExporter(private val context: Context) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/
    
    private val filesDir = context.applicationContext.filesDir

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/
    
    /**
     * Export collection content to a zip file
     *
     * @property destination uri of the directory where should create the zip
     * @property tracts list of tract to export
     * @property pictures list of pictures to export
     */
    fun export(destination: Uri, tracts: List<Tract>, pictures: List<TractPicture>) {
        Log.i(TAG, "Export tract and pictures collections")

        export(tracts, CollectionFiles.TRACT_LIST_JSON_FILENAME)
        export(pictures, CollectionFiles.PICTURE_LIST_JSON_FILENAME)

        val files = CollectionFiles.REQUIRED_FILES_IN_ZIP +
                pictures.map { it.photoFilename.toUri().lastPathSegment ?: "" }

        getDestinationFileUri(destination)?.let { uri ->
            Zipper(context).zip(uri, files.map { File(filesDir, it) }.toTypedArray())
        }
    }
    
    /***********************************************************************************************
     * Tools
     **********************************************************************************************/

    private fun getDestinationFileUri(directoryUri: Uri): Uri? {
        val documentFile = DocumentFile.fromTreeUri(context, directoryUri)
        val destinationFile =
            documentFile?.createFile(MimeType.ZIP.string, CollectionFiles.ZIP_FILENAME)

        return destinationFile?.uri
    }

    private fun export(objectToExport: Any, filename: String) {
        val file = File(filesDir, filename)
        val result = objectToExport.toJson()

        file.writeText(result)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        private val TAG = CollectionExporter::class.simpleName ?: "Default"
     }

}