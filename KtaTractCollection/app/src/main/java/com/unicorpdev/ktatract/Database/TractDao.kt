package com.unicorpdev.ktatract.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unicorpdev.ktatract.models.Tract
import java.util.*

@Dao
interface TractDao {

    /***********************************************************************************************
     * Query
     **********************************************************************************************/
    
    @Query("SELECT * FROM tract")
    fun getTracts(): LiveData<List<Tract>>

    @Query("SELECT * FROM tract WHERE id=(:id)")
    fun getTract(id: UUID): LiveData<Tract?>

    /***********************************************************************************************
     * Update
     **********************************************************************************************/
    
    @Update
    fun updateTract(tract: Tract)

    /***********************************************************************************************
     * Insert
     **********************************************************************************************/
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTract(tract: Tract)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun addTracts(tracts: List<Tract>)

    /***********************************************************************************************
     * Delete
     **********************************************************************************************/

    @Delete
    fun deleteTract(tract: Tract)

}