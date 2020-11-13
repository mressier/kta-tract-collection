package com.unicorpdev.ktatract.shared.tools.collection

internal class CollectionFiles {
    companion object {
        const val TRACT_LIST_JSON_FILENAME = "tractList.json"
        const val PICTURE_LIST_JSON_FILENAME = "pictureList.json"
        const val ZIP_FILENAME = "export.zip"

        val REQUIRED_FILES_IN_ZIP =
            arrayOf(PICTURE_LIST_JSON_FILENAME, TRACT_LIST_JSON_FILENAME)
    }
}