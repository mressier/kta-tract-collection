package com.unicorpdev.ktatract.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture

@Database(entities = [Tract::class, TractPicture::class, TractCollection::class], version = 1)
@TypeConverters(TractTypeConverters::class)
abstract class TractDatabase: RoomDatabase() {

    abstract fun tractDao(): TractDao

    abstract fun pictureDao(): PictureDao

    abstract fun collectionDao(): CollectionDao
}