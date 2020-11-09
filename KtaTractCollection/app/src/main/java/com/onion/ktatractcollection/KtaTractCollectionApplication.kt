package com.onion.ktatractcollection

import android.app.Application
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.shared.tools.DatabaseZipper

class KtaTractCollectionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TractRepository.initialize(this)
        DatabaseZipper.initialize(this)
    }
}