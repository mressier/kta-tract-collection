package com.unicorpdev.ktatract.utils

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.unicorpdev.ktatract.database.CollectionDao
import com.unicorpdev.ktatract.database.PictureDao
import com.unicorpdev.ktatract.database.TractDao
import com.unicorpdev.ktatract.database.TractDatabase

class DatabaseMock {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/
    
    private lateinit var context: Context
    private lateinit var db: TractDatabase

    lateinit var tractDao: TractDao
    lateinit var pictureDao: PictureDao
    lateinit var collectionDao: CollectionDao

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/
    
    fun create() {
        context  = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, TractDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tractDao = db.tractDao()
        pictureDao = db.pictureDao()
        collectionDao = db.collectionDao()
    }
    
    fun close() {
        db.close()
    }
}