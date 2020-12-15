package com.unicorpdev.ktatract.database

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

class TractRepository private constructor(context: Context) {

    private val DATABASE_NAME = "tract-repository"

    /**
     * Properties
     */
    private val database: TractDatabase = Room.databaseBuilder(
        context.applicationContext,
        TractDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val tractDao = database.tractDao()

    private val pictureDao = database.pictureDao()

    private val collectionDao = database.collectionDao()

    private val executor = Executors.newSingleThreadExecutor()

    private val filesDir = context.applicationContext.filesDir

    /***********************************************************************************************
     * Tracts
     **********************************************************************************************/

    fun getTracts(): LiveData<List<Tract>> = tractDao.getTractsLiveData()

    fun getTract(id: UUID): LiveData<Tract?> = tractDao.getTractLiveData(id)

    fun getTracts(collectionId: UUID): LiveData<List<Tract>> =
        tractDao.getTractsForCollectionLiveData(collectionId)

    fun addTract(tract: Tract) {
        executor.execute { tractDao.addTract(tract) }
    }

    fun addTracts(tracts: List<Tract>) {
        executor.execute { tractDao.addTracts(tracts) }
    }

    fun addEmptyTract(): UUID {
        val tract = Tract()
        addTract(tract)
        return tract.id
    }

    fun updateTract(tract: Tract) {
        executor.execute { tractDao.updateTract(tract) }
    }

    fun deleteTract(tract: Tract) {
        deletePicturesForTract(tract.id)
        executor.execute() { tractDao.deleteTract(tract) }
    }

    /***********************************************************************************************
     * Pictures
     **********************************************************************************************/
    
    fun getPictures(tractId: UUID): LiveData<List<TractPicture>> =
        pictureDao.getPicturesForTractLiveData(tractId)

    fun getPictures(): LiveData<List<TractPicture>> =
        pictureDao.getPicturesLiveData()

    fun getPictureFile(filename: String): File = File(filesDir, filename)

    fun addPicture(picture: TractPicture) {
        executor.execute() { pictureDao.addPicture(picture) }
    }

    fun addPictures(pictures: List<TractPicture>) {
        executor.execute() { pictureDao.addPictures(pictures) }
    }

    /***********************************************************************************************
     * Pictures - Delete
     **********************************************************************************************/

    fun deletePicture(picture: TractPicture) {
        executor.execute() {
            deletePictureFile(picture.photoFilename)
            pictureDao.deletePicture(picture)
        }
    }

    private fun deletePictures(pictures: List<TractPicture>) {
        executor.execute {
            deletePicturesFile(pictures.map { it.photoFilename })
            pictureDao.deletePictures(pictures)
        }
    }

    private fun deletePicturesForTract(tractId: UUID) {
        val tractPictures = pictureDao.getPicturesForTract(tractId)
        deletePictures(tractPictures)
        executor.execute { pictureDao.deletePicturesForTract(tractId) }
    }

    private fun deletePicturesFile(paths: List<String>) {
        paths.forEach { deletePictureFile(it) }
    }

    private fun deletePictureFile(path: String) {
        val uri = Uri.parse(path)
        val file = uri.lastPathSegment?.let { filename ->
            getPictureFile(filename)
        }
        file?.delete()
    }
    
    /***********************************************************************************************
     * Collections
     **********************************************************************************************/

    fun getCollections(): LiveData<List<TractCollection>> =
        collectionDao.getCollectionsLiveData()

    fun updateCollection(collection: TractCollection) {
        executor.execute { collectionDao.updateCollection(collection) }
    }

    fun deleteCollection(collection: TractCollection) {
        executor.execute {
            pictureDao.deletePictureForCollection(collection.id)
            tractDao.deleteTractForCollection(collection.id)
            collectionDao.deleteCollection(collection)
        }
    }

    fun addCollection(collection: TractCollection) {
        executor.execute { collectionDao.addCollection(collection) }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {

        private var INSTANCE: TractRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TractRepository(context)
            }
        }

        fun get(): TractRepository {
            return INSTANCE ?: throw IllegalStateException("TractRepository must be initialized")
        }
    }
}
