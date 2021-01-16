package com.unicorpdev.ktatract.database

import android.content.Context
import android.net.Uri
import com.unicorpdev.ktatract.shared.extensions.toJson
import com.unicorpdev.ktatract.shared.tools.export.ObjectToExport
import com.unicorpdev.ktatract.shared.tools.zip.Zipper
import java.io.File

class ObjectExporter(context: Context) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val zipper = Zipper(context)
    private val filesDir = context.applicationContext.filesDir

    /***********************************************************************************************
     * ZipMethods
     **********************************************************************************************/

    fun zip(destinationUri: Uri, objects: Array<ObjectToExport>) {
        export(objects)

        val allFiles = objects.flatMap { it.relatedFilesPath } +
                objects.map { it.filename }
        zipper.zip(destinationUri, allFiles.toTypedArray())
    }

    /***********************************************************************************************
     * ExportMethods
     **********************************************************************************************/

    fun export(myObject: ObjectToExport) {
        val file = File(filesDir, myObject.filename)
        val result = myObject.content.toJson()

        file.writeText(result)
    }

    fun export(objects: Array<ObjectToExport>) {
        objects.forEach { export(it) }
    }
}