package com.unicorpdev.ktatract

import android.app.Application
import com.unicorpdev.ktatract.Database.TractRepository

class KtaTractCollectionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TractRepository.initialize(this)
    }
}