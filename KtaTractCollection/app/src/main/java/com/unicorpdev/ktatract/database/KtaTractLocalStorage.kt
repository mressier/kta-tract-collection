package com.unicorpdev.ktatract.database

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class KtaTractLocalStorage(private val context: Context) {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val storage: SharedPreferences by lazy {
        context.getSharedPreferences("KtaTractStorage", Context.MODE_PRIVATE)
    }

    /** Access **/

    var defaultCollectionId: UUID
        get() {
            val value = storage.getString(COLLECTION_ID, "") ?: ""
            return UUID.fromString(value)
        }
        set(value) {
            with(storage.edit()) {
                putString(COLLECTION_ID, value.toString())
                apply()
            }
        }


    
    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        /** Keys **/

        private const val COLLECTION_ID = "collection_id_key"

        /** Instance **/

        private lateinit var localStorage: KtaTractLocalStorage

        /** Initialization **/

        fun init(context: Context) {
            localStorage = KtaTractLocalStorage(context)
        }

        fun getInstance(): KtaTractLocalStorage {
            return localStorage
        }
    }
}