package com.onion.ktatractcollection.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
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

    private val executor = Executors.newSingleThreadExecutor()

    private val filesDir = context.applicationContext.filesDir

    /**
     * Tract Methods
     */

    fun getTracts(): LiveData<List<Tract>> = tractDao.getTracts()

    fun getTract(id: UUID): LiveData<Tract?> = tractDao.getTract(id)

    fun addTract(tract: Tract) {
        executor.execute() { tractDao.addTract(tract) }
    }

    fun updateTract(tract: Tract) {
        executor.execute() { tractDao.updateTract(tract) }
    }

    fun deleteTract(tract: Tract) {
        executor.execute() {
            tractDao.deleteTract(tract)
            tractDao.deletePicturesForTract(tract.id)
        }
    }

    /**
     * Pictures
     */

    fun getPictures(tractId: UUID): LiveData<List<TractPicture>> =
        tractDao.getPicturesForTract(tractId)

    fun getPicturesFile(pictures: List<TractPicture>): List<File> =
        pictures.map { getPictureFile(it.photoFilename) }

    fun getPictureFile(filename: String): File = File(filesDir, filename)

    fun deletePicturesFiles(files: List<File>) {
        executor.execute() {
            files.forEach { it.delete() }
        }
    }

    fun addPicture(picture: TractPicture) {
        executor.execute() { tractDao.addPicture(picture) }
    }

    fun deletePicture(picture: TractPicture) {
        executor.execute() {
            getPictureFile(picture.photoFilename).delete()
            tractDao.deletePicture(picture)
        }
    }

    /**
     * Instance
     */
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
