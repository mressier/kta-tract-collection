package com.unicorpdev.ktatract.shared.analytics

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class KtaTractAnalytics {

    enum class SelectEvent {
        SEARCH,
        LIST_DISPLAY,
        SORT_LIST,
        EXPORT_COLLECTION,
        IMPORT_COLLECTION,
        ABOUT,
        IMPORT_MULTIPLE,
        IMPORT_ONE_GALLERY,
        IMPORT_ONE_PICTURE,
        LIKE,
        DELETE_TRACT,
        DELETE_PICTURE,
        MODIFY_TRACT
    }

    companion object {

        /*******************************************************************************************
         * Properties
         ******************************************************************************************/

        var firebaseAnalytics: FirebaseAnalytics? = null

        /*******************************************************************************************
         * Methods
         ******************************************************************************************/

        fun initialize(app: Application) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(app)
        }

        fun logSelectItem(item: SelectEvent) {
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                param(FirebaseAnalytics.Param.ITEM_ID, item.ordinal.toLong())
                param(FirebaseAnalytics.Param.ITEM_NAME, item.name)
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
        }

        fun logSearch(searchTerm: String) {
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                param(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)
            }
        }
    }
}