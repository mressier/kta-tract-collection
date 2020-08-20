package com.onion.ktatractcollection.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onion.ktatractcollection.Models.Tract

@Database(entities = [Tract::class], version = 1)
@TypeConverters(TractTypeConverters::class)
abstract class TractDatabase: RoomDatabase() {

    abstract fun tractDao(): TractDao
}