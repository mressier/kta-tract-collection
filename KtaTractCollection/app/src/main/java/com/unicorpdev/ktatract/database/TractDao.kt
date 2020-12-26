package com.unicorpdev.ktatract.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unicorpdev.ktatract.models.Tract
import java.util.*

@Dao
interface TractDao {

    /***********************************************************************************************
     * Query - Live Data
     **********************************************************************************************/

    @Query("SELECT * FROM tract")
    fun getTractsLiveData(): LiveData<List<Tract>>

    @Query("SELECT * FROM tract WHERE id=(:id)")
    fun getTractLiveData(id: UUID): LiveData<Tract?>

    @Query("SELECT * FROM tract WHERE collectionId=(:collectionId)")
    fun getTractsForCollectionLiveData(collectionId: UUID): LiveData<List<Tract>>

    /***********************************************************************************************
     * Query - Raw Data
     **********************************************************************************************/

    @Query("SELECT * FROM tract")
    fun getTracts(): List<Tract>

    @Query("SELECT * FROM tract WHERE id=(:id)")
    fun getTract(id: UUID): Tract?

    @Query("SELECT * FROM tract WHERE id IN (:ids)")
    fun getTracts(ids: List<UUID>): List<Tract>

    @Query("SELECT * FROM tract WHERE collectionId=(:collectionId)")
    fun getTractsForCollection(collectionId: UUID): List<Tract>

    /***********************************************************************************************
     * Update
     **********************************************************************************************/
    
    @Update
    fun updateTract(tract: Tract)

    /***********************************************************************************************
     * Insert
     **********************************************************************************************/
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTract(tract: Tract)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun addTracts(tracts: List<Tract>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun addTractsIfNotExist(tracts: List<Tract>)

    /***********************************************************************************************
     * Delete
     **********************************************************************************************/

    @Delete
    fun deleteTract(tract: Tract)

    @Query("DELETE FROM tract WHERE collectionId=(:collectionId)")
    fun deleteTractForCollection(collectionId: UUID)
}