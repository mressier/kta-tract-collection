package com.onion.ktatractcollection.shared.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> T.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.jsonToObject(): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(this, type)
}
