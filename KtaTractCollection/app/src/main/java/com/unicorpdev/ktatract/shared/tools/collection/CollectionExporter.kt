package com.unicorpdev.ktatract.shared.tools.collection

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.unicorpdev.ktatract.database.ObjectExporter
import com.unicorpdev.ktatract.models.MimeType
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.tools.export.ObjectToExport

/**
 * Export a collection of tracts and pictures to a zip file
 */
class CollectionExporter(private val context: Context) {

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
    fun export(
        destination: Uri,
        collections: List<TractCollection>,
        tracts: List<Tract>,
        pictures: List<TractPicture>
    ) {
        Log.i(TAG, "Export tract and pictures collections")

        val tables = arrayOf(
            ObjectToExport(
                CollectionFiles.COLLECTION_LIST_JSON_FILENAME,
                collections,
                collections.mapNotNull { it.imageFilename?.toUri()?.lastPathSegment }
            ),
            ObjectToExport(
                CollectionFiles.TRACT_LIST_JSON_FILENAME,
                tracts,
                listOf()
            ),
            ObjectToExport(
                CollectionFiles.PICTURE_LIST_JSON_FILENAME,
                pictures,
                pictures.mapNotNull { it.photoFilename.toUri().lastPathSegment }
            )
        )

        val destinationFilename = getExportFilename(collections)
        val destinationUri = getDestinationFileUri(destination, destinationFilename)

        if (destinationUri != null) {
            ObjectExporter(context).zip(destinationUri, tables)
        }

    }

    /***********************************************************************************************
     * Tools
     **********************************************************************************************/

    private fun getDestinationFileUri(directoryUri: Uri, filename: String): Uri? {
        val documentFile = DocumentFile.fromTreeUri(context, directoryUri)
        val destinationFile =
            documentFile?.createFile(MimeType.ZIP.string, filename)

        return destinationFile?.uri
    }

    private fun getExportFilename(collections: List<TractCollection>): String {
        val filename =
            if (collections.size == 1) collections.first().title else CollectionFiles.ZIP_FILENAME
        return filename + CollectionFiles.ZIP_EXTENSION
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        private val TAG = CollectionExporter::class.simpleName ?: "Default"
     }

}