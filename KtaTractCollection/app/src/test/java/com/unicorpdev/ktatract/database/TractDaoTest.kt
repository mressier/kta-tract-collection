package com.unicorpdev.ktatract.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.utils.DatabaseMock
import com.unicorpdev.ktatract.utils.newTract
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TractDaoTest {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var tractDao: TractDao
    private var db = DatabaseMock()

    /***********************************************************************************************
     * Initialization
     **********************************************************************************************/
    
    @Before
    fun createDb() {
        db.create()
        tractDao = db.tractDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /***********************************************************************************************
     * Tracts
     **********************************************************************************************/

    @Test
    fun testAddTract() {
        val author = "George"
        val tract = newTract().apply { this.author = author }

        // When
        tractDao.addTract(tract)

        // Then
        val findTract = tractDao.getTract(tract.id)
        assertThat(findTract).isNotNull()
        assertThat(findTract?.author).isEqualTo(author)
    }

    @Test
    fun testGetTracts() {
        val authors = listOf("George", "Fred", "Lily")
        val tracts = authors.map { newTract().apply { this.author = it } }.toList()

        // when
        tractDao.addTracts(tracts)

        // Then
        val findTractList = tractDao.getTracts()
        assertThat(findTractList.size).isEqualTo(authors.size)
    }

    @Test
    fun testGetTractsWithIds() {
        val authors = listOf("George", "Fred", "Lily")
        val tracts = authors.map { newTract().apply { this.author = it } }.toList()

        // when
        tractDao.addTracts(tracts)

        // Then
        val findTractsList = tractDao.getTracts(tracts.map { it.id })
        assertThat(findTractsList.size).isEqualTo(authors.size)

        val findTractsList2 = tractDao.getTracts(listOf(tracts.first().id))
        assertThat(findTractsList2.size).isEqualTo(1)
    }

    @Test
    fun testUpdateTracts() {
        val authors = listOf("George", "Fred", "Lily")
        val tracts = authors.map { newTract().apply { this.author = it } }.toList()

        tractDao.addTracts(tracts)

        assertThat(tractDao.getTract(tracts[0].id)).isEqualTo(tracts[0])

        val tract = tracts[0].copy().apply { author = "Harry" }

        // When
        tractDao.updateTract(tract)

        // Then
        val newTract = tractDao.getTract(tracts[0].id)
        assertThat(newTract?.author).isNotEqualTo(tracts[0].author)
        assertThat(newTract?.author).isEqualTo(tract.author)

        assertThat(tractDao.getTract(tracts[1].id)).isEqualTo(tracts[1])
        assertThat(tractDao.getTract(tracts[2].id)).isEqualTo(tracts[2])
    }

    @Test
    fun testRemoveTract() {
        val authors = listOf("George", "Fred", "Lily")
        val tracts = authors.map { newTract().apply { this.author = it } }.toList()

        tractDao.addTracts(tracts)

        val firstCheck = tractDao.getTracts()
        assertThat(firstCheck.size).isEqualTo(authors.size)

        // When
        tractDao.deleteTract(tracts[0])

        // Then
        val findTractList = tractDao.getTracts()
        assertThat(findTractList.size).isEqualTo(tracts.size - 1)
    }

    /***********************************************************************************************
     * Tract with Collection
     **********************************************************************************************/

    @Test
    fun testGetTractForCollection() {
        val collection = TractCollection()
        val collection2 = TractCollection()
        val tracts = listOf(
            Tract(author = "George 1", collectionId = collection.id),
            Tract(author = "George 2", collectionId = collection.id),
            Tract(author = "Fred", collectionId = collection2.id),
            Tract(author = "Fred 2", collectionId = collection2.id),
            Tract(author = "George 3", collectionId = collection.id),
        )

        tractDao.addTracts(tracts)

        // When
        val tractList = tractDao.getTracts()
        assertThat(tractList.size).isEqualTo(tracts.size)

        val tractOnCollection = tractDao.getTractsForCollection(collection.id)
        val tractWithDefaultCollection = tractDao.getTractsForCollection(collection2.id)

        // Then
        assertThat(tractOnCollection.size).isEqualTo(3)
        assertThat(tractWithDefaultCollection.size).isEqualTo(2)
    }

    @Test
    fun removeTractForCollection() {
        val collection = TractCollection()
        val collection2 = TractCollection()
        val tracts = listOf(
            Tract(author = "George 1", collectionId = collection.id),
            Tract(author = "George 2", collectionId = collection.id),
            Tract(author = "Fred", collectionId = collection2.id),
            Tract(author = "Fred 2", collectionId = collection2.id),
            Tract(author = "George 3", collectionId = collection.id),
        )

        tractDao.addTracts(tracts)

        // When
        tractDao.deleteTractForCollection(collection.id)

        val tractOnCollection = tractDao.getTractsForCollection(collection.id)
        val tractWithDefaultCollection = tractDao.getTractsForCollection(collection2.id)

        // Then
        assertThat(tractOnCollection.size).isEqualTo(0)
        assertThat(tractWithDefaultCollection.size).isEqualTo(2)

        // When
        tractDao.deleteTractForCollection(collection2.id)

        // Then
        assertThat(tractDao.getTractsForCollection(collection2.id).size).isEqualTo(0)
    }
}