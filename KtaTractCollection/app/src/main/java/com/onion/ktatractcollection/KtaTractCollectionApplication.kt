package com.onion.ktatractcollection

import android.app.Application
import com.onion.ktatractcollection.Database.TractRepository

class KtaTractCollectionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TractRepository.initialize(this)
    }
}