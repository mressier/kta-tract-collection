package com.unicorpdev.ktatract

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.unicorpdev.ktatract.Database.TractRepository
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics

/**
 * Main Class for KtaTract Application
 *
 * Init singleton and everything else required in the whole app
 */
class KtaTractCollectionApplication: Application() {

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate() {
        super.onCreate()

        setupRepository()
        setupFirebase()
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupRepository() {
        TractRepository.initialize(this)
    }

    private fun setupFirebase() {
        KtaTractAnalytics.initialize(this)
    }
}