package com.onion.ktatractcollection.shared.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> List<T>.toJson(): String {
    return Gson().toJson(this)
}

fun <T> String.jsonToList(): List<T> {
    val type = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(this, type)
}
