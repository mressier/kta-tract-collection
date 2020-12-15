package com.unicorpdev.ktatract.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.unicorpdev.ktatract.models.TractCollection
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CollectionDaoTest {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var collectionDao: CollectionDao

    private lateinit var db: TractDatabase

    /***********************************************************************************************
     * Initialization
     **********************************************************************************************/

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(context, TractDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        collectionDao = db.collectionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /***********************************************************************************************
     * Pictures - Add
     **********************************************************************************************/

    @Test
    fun testAddCollection() {
        val collection = TractCollection()

        // When
        collectionDao.addCollection(collection)

        // Then
        assertThat(collectionDao.getCollections().size).isEqualTo(1)
        assertThat(collectionDao.getCollection(collection.id)).isEqualTo(collection)
    }

    @Test
    fun testAddCollections() {
        val collections = listOf(TractCollection(),  TractCollection())

        // When
        collectionDao.addCollections(collections)

        // Then
        assertThat(collectionDao.getCollections().size).isEqualTo(2)
        assertThat(collectionDao.getCollection(collections[0].id)).isEqualTo(collections[0])
    }
    
    /***********************************************************************************************
     * Remove
     **********************************************************************************************/

    @Test
    fun testRemoveCollection() {
        val collections = listOf(TractCollection(),  TractCollection())

        collectionDao.addCollections(collections)

        // When
        collectionDao.deleteCollection(collections[0])

        // Then
        assertThat(collectionDao.getCollections().size).isEqualTo(1)
        assertThat(collectionDao.getCollection(collections[0].id)).isEqualTo(null)
    }
}