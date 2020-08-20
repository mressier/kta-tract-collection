package com.onion.ktatractcollection.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.onion.ktatractcollection.Models.Tract
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "tract-repository"

class TractRepository private constructor(context: Context) {

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

    /**
     * Methods
     */
    // Implement database access functions
    fun getTracts(): LiveData<List<Tract>> = tractDao.getTracts()

    fun getTract(id: UUID): LiveData<Tract?> = tractDao.getTract(id)

    fun updateTract(tract: Tract) {
        executor.execute() { tractDao.updateTract(tract) }
    }

    fun addTract(tract: Tract) {
        executor.execute() { tractDao.addTract(tract) }
    }

    fun deleteTract(tract: Tract) {
        executor.execute() { tractDao.deleteTract(tract) }
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