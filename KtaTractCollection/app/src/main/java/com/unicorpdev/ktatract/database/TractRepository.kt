package com.unicorpdev.ktatract.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.shared.extensions.toInt
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

class TractRepository private constructor(var context: Context): RoomDatabase.Callback() {

    val filesDir = context.applicationContext.filesDir

    /**
     * Properties
     */

    private var database: TractDatabase =
        Room.databaseBuilder(context.applicationContext, TractDatabase::class.java, DATABASE_NAME)
            .addCallback(this)
            .build()

    private val tractDao = database.tractDao()

    private val pictureDao = database.pictureDao()

    private val collectionDao = database.collectionDao()

    private val executor = Executors.newSingleThreadExecutor()

    private val localStorage: KtaTractLocalStorage by lazy {
        KtaTractLocalStorage.getInstance()
    }

    /***********************************************************************************************
     * Database Life Cycle
     **********************************************************************************************/

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        Log.d(TAG, "On create database")

        val defaultCollection = TractCollection(
            title = context.getString(R.string.my_collection_title),
            description = context.getString(R.string.my_collection_description)
        )

        db.execSQL("INSERT INTO tractcollection " +
                "(id, title, description, imageFilename) " +
                "VALUES (" +
                "\"${defaultCollection.id}\", " +
                "\"${defaultCollection.title}\", " +
                "\"${defaultCollection.description}\", " +
                "NULL" +
                ");")

        localStorage.defaultCollectionId = defaultCollection.id
    }

    /***********************************************************************************************
     * Tracts - Get Live Data
     **********************************************************************************************/

    fun getTractsLiveData(): LiveData<List<Tract>> = tractDao.getTractsLiveData()

    fun getTractLiveData(id: UUID): LiveData<Tract?> = tractDao.getTractLiveData(id)

    fun getTractsForCollectionLiveData(collectionId: UUID): LiveData<List<Tract>> =
        tractDao.getTractsForCollectionLiveData(collectionId)

    /***********************************************************************************************
     * Tracts - Get Raw Data
     **********************************************************************************************/

    fun getTracts(): List<Tract> = tractDao.getTracts()

    fun getTract(id: UUID): Tract? = tractDao.getTract(id)

    fun getTracts(ids: List<UUID>): List<Tract> = tractDao.getTracts(ids)

    fun getTractsForCollection(collectionId: UUID): List<Tract> =
        tractDao.getTractsForCollection(collectionId)

    fun getEmptyTract(collectionId: UUID? = null): Tract {
        val tractCollectionId =
            collectionId ?: KtaTractLocalStorage.getInstance().defaultCollectionId
        return Tract(collectionId = tractCollectionId)
    }

    /***********************************************************************************************
     * Tracts - Add / Update / Delete
     **********************************************************************************************/

    fun addTract(tract: Tract) {
        executor.execute { tractDao.addTract(tract) }
    }

    fun addTracts(tracts: List<Tract>) {
        executor.execute { tractDao.addTracts(tracts) }
    }

    fun addTractsIfNotExists(tracts: List<Tract>) {
        executor.execute { tractDao.addTractsIfNotExist(tracts) }
    }

    fun addEmptyTract(collectionId: UUID? = null): UUID {
        val tract = getEmptyTract(collectionId)

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
     * Pictures - Get Live Data
     **********************************************************************************************/

    fun getPicturesLiveData(tractId: UUID): LiveData<List<TractPicture>> =
        pictureDao.getPicturesForTractLiveData(tractId)

    fun getPicturesLiveData(): LiveData<List<TractPicture>> =
        pictureDao.getPicturesLiveData()

    fun getPicturesForCollectionLiveData(collectionId: UUID): LiveData<List<TractPicture>> =
        pictureDao.getPicturesForCollectionLiveData(collectionId)

    /***********************************************************************************************
     * Pictures - Get Raw Data
     **********************************************************************************************/

    fun getPictures(tractId: UUID): List<TractPicture> =
        pictureDao.getPicturesForTract(tractId)

    fun getPictures(): List<TractPicture> =
        pictureDao.getPictures()

    fun getPicturesForCollection(collectionId: UUID): List<TractPicture> =
        pictureDao.getPicturesForCollection(collectionId)

    fun getPictureFile(filename: String): File = File(filesDir, filename)

    /***********************************************************************************************
     * Pictures - Add
     **********************************************************************************************/

    fun addPicture(picture: TractPicture) {
        executor.execute() { pictureDao.addPicture(picture) }
    }

    fun addPictures(pictures: List<TractPicture>) {
        executor.execute() { pictureDao.addPictures(pictures) }
    }

    fun addPicturesIfNotExist(pictures: List<TractPicture>) {
        executor.execute() { pictureDao.addPicturesIfNotExist(pictures) }
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
        executor.execute {
            val tractPictures = pictureDao.getPicturesForTract(tractId)
            deletePictures(tractPictures)
            pictureDao.deletePicturesForTract(tractId)
        }
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
     * Collections - Get Live Data
     **********************************************************************************************/

    fun getCollectionsLiveData(): LiveData<List<TractCollection>> =
        collectionDao.getCollectionsLiveData()

    fun getCollectionLiveData(collectionId: UUID): LiveData<TractCollection?> =
        collectionDao.getCollectionLiveData(collectionId)

    /***********************************************************************************************
     * Collections - Get Raw Data
     **********************************************************************************************/

    fun getCollections(): List<TractCollection> = collectionDao.getCollections()

    fun getCollection(collectionId: UUID): TractCollection? =
        collectionDao.getCollection(collectionId)

    fun getCollections(collectionIds: List<UUID>): List<TractCollection> =
        collectionDao.getCollections(collectionIds)

    val defaultCollectionId: UUID
    get() = localStorage.defaultCollectionId

    /***********************************************************************************************
     * Collections - Update
     **********************************************************************************************/

    fun updateCollection(collection: TractCollection) {
        executor.execute { collectionDao.updateCollection(collection) }
    }

    /***********************************************************************************************
     * Collections - Delete
     **********************************************************************************************/

    fun deleteCollection(collection: TractCollection) {
        executor.execute {
            pictureDao.deletePictureForCollection(collection.id)
            tractDao.deleteTractForCollection(collection.id)
            collectionDao.deleteCollection(collection)
        }
    }

    /***********************************************************************************************
     * Collections - Add
     **********************************************************************************************/

    fun addCollection(collection: TractCollection) {
        executor.execute { collectionDao.addCollection(collection) }
    }

    fun addCollections(collections: List<TractCollection>) {
        executor.execute { collectionDao.addCollections(collections) }
    }

    fun addCollectionsIfNotExist(collections: List<TractCollection>) {
        executor.execute { collectionDao.addCollectionsIfNotExist(collections) }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        private val TAG = TractRepository::class.simpleName ?: "Default"

        const val DATABASE_NAME = "tract-repository.db"

        /** Instance **/

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
