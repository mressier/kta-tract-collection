package com.unicorpdev.ktatract.shared.tools.collection

internal class CollectionFiles {
    companion object {
        const val COLLECTION_LIST_JSON_FILENAME = "collectionList.json"
        const val TRACT_LIST_JSON_FILENAME = "tractList.json"
        const val PICTURE_LIST_JSON_FILENAME = "pictureList.json"
        const val ZIP_FILENAME = "ktatract-export"
        const val ZIP_EXTENSION = ".zip"

        val REQUIRED_FILES_IN_ZIP = arrayOf(
            COLLECTION_LIST_JSON_FILENAME,
            PICTURE_LIST_JSON_FILENAME,
            TRACT_LIST_JSON_FILENAME
        )
    }
}