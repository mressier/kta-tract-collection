package com.unicorpdev.ktatract.shared.tools

import com.unicorpdev.ktatract.shared.extensions.jsonToObject
import java.io.File
import java.io.FileNotFoundException

class JsonFile(filename: String): File(filename) {

    inline fun <reified T> getObject(): T? {
        if (!this.exists()) {
            throw FileNotFoundException(this.absolutePath)
        }
        val result = readText()

        return result.jsonToObject()
    }
}
