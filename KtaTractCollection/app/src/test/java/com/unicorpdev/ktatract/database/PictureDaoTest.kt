package com.unicorpdev.ktatract.database

import android.content.Context
import android.graphics.Picture
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.utils.DatabaseMock
import com.unicorpdev.ktatract.utils.newTract
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class PictureDaoTest {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var tractDao: TractDao
    private lateinit var pictureDao: PictureDao
    private var db = DatabaseMock()

    /***********************************************************************************************
     * Initialization
     **********************************************************************************************/

    @Before
    fun createDb() {
        db.create()
        tractDao = db.tractDao
        pictureDao = db.pictureDao
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
    fun testAddPicture() {
        val tract = newTract()
        val picture = TractPicture(tractId = tract.id)

        // When
        pictureDao.addPicture(picture)

        // Then
        assertThat(pictureDao.getPictures().size).isEqualTo(1)
        assertThat(pictureDao.getPictures().first().id).isEqualTo(picture.id)
    }

    @Test
    fun testAddPictureWithConflict() {
        val tract = newTract()
        val picture = TractPicture(tractId = tract.id, photoFilename = "first")
        val picture2 = TractPicture(id = picture.id, tractId = tract.id, photoFilename = "second")

        // When
        pictureDao.addPicture(picture)
        pictureDao.addPicture(picture2)

        // Then
        val pictures = pictureDao.getPictures()
        val firstPicture = pictures.first()

        assertThat(pictures.size).isEqualTo(1)
        // New picture should replace the old one
        assertThat(firstPicture.id).isEqualTo(picture.id)
        assertThat(firstPicture.photoFilename).isEqualTo(picture2.photoFilename)
        assertThat(firstPicture.photoFilename).isNotEqualTo(picture.photoFilename)
    }
    
    /***********************************************************************************************
     * Pictures - Delete
     **********************************************************************************************/

    @Test
    fun testDeletePicture() {
        val tract = newTract()
        val pictures = listOf(TractPicture(tractId = tract.id), TractPicture(tractId = tract.id))

        pictureDao.addPictures(pictures)
        assertThat(pictureDao.getPictures().size).isEqualTo(pictures.size)

        // When
        pictureDao.deletePicture(pictures[0])

        // Then
        assertThat(pictureDao.getPictures().size).isEqualTo(pictures.size - 1)
        assertThat(pictureDao.getPictures().first().id).isEqualTo(pictures[1].id)
    }

    /***********************************************************************************************
     * Pictures - With Tracts
     **********************************************************************************************/

    @Test
    fun testGetPictureForTract() {
        val tracts = listOf(newTract(), newTract())
        val pictures = listOf(
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[0].id)
        )

        // When
        pictureDao.addPictures(pictures)

        // Then
        assertThat(pictureDao.getPictures().size).isEqualTo(pictures.size)

        assertThat(pictureDao.getPicturesForTract(tracts[0].id).size).isEqualTo(3)
        assertThat(pictureDao.getPicturesForTract(tracts[1].id).size).isEqualTo(1)

        assertThat(pictureDao.getPicturesForTract(UUID.randomUUID()).size).isEqualTo(0)
    }

    @Test
    fun testDeletePictureForTract() {
        val tracts = listOf(newTract(), newTract())
        val pictures = listOf(
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[0].id)
        )

        pictureDao.addPictures(pictures)

        // When
        pictureDao.deletePicturesForTract(tracts[1].id)

        // Then
        assertThat(pictureDao.getPictures().size).isEqualTo(pictures.size - 1)

        assertThat(pictureDao.getPicturesForTract(tracts[0].id).size).isEqualTo(3)
        assertThat(pictureDao.getPicturesForTract(tracts[1].id).size).isEqualTo(0)

    }

    /***********************************************************************************************
     * Pictures - With Collection
     **********************************************************************************************/

    @Test
    fun testGetPicturesForCollection() {
        val collection = TractCollection()
        val tracts = listOf(
            newTract(),
            Tract(collectionId = collection.id),
            Tract(collectionId = collection.id)
        )
        val pictures = listOf(
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[2].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[2].id),
        )

        // When
        tractDao.addTracts(tracts)
        pictureDao.addPictures(pictures)

        // Then
        assertThat(pictureDao.getPictures().size).isEqualTo(pictures.size)
        assertThat(pictureDao.getPicturesForCollection(collection.id).size).isEqualTo(4)
    }

    @Test
    fun testDeletePicturesForCollection() {
        val collection = TractCollection()
        val tracts = listOf(
            newTract(),
            Tract(collectionId = collection.id),
            Tract(collectionId = collection.id)
        )
        val pictures = listOf(
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[2].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[0].id),
            TractPicture(tractId = tracts[1].id),
            TractPicture(tractId = tracts[2].id),
        )

        tractDao.addTracts(tracts)
        pictureDao.addPictures(pictures)

        // When
        pictureDao.deletePictureForCollection(collection.id)

        // Then
        assertThat(pictureDao.getPicturesForCollection(collection.id).size).isEqualTo(0)
        assertThat(pictureDao.getPictures().size).isEqualTo(3)
    }
}