package com.onion.ktatractcollection.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import java.util.*

@Dao
interface TractDao {

    /**
     * Tract
     */
    @Query("SELECT * FROM tract")
    fun getTracts(): LiveData<List<Tract>>

    @Query("SELECT * FROM tract WHERE id=(:id)")
    fun getTract(id: UUID): LiveData<Tract?>

    @Update
    fun updateTract(tract: Tract)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTract(tract: Tract)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun addTracts(tracts: List<Tract>)

    @Delete
    fun deleteTract(tract: Tract)

}