package com.unicorpdev.ktatract.shared.tools

import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.shared.extensions.jsonToObject
import java.io.File
import java.io.FileNotFoundException

class JsonFile(directory: File?, filename: String): File(directory, filename) {

    inline fun <reified T> getObject(): T? {
        if (!this.exists()) {
            throw FileNotFoundException(this.absolutePath)
        }
        val result = this.readText()
        println(result)

        return result.jsonToObject()
    }
}
