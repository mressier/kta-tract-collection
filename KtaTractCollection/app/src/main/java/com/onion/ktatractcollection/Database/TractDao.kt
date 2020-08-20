package com.onion.ktatractcollection.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.onion.ktatractcollection.Models.Tract
import java.util.*

@Dao
interface TractDao {

    @Query("SELECT * FROM tract")
    fun getTracts(): LiveData<List<Tract>>

    @Query("SELECT * FROM tract WHERE id=(:id)")
    fun getTract(id: UUID): LiveData<Tract?>

    @Update
    fun updateTract(tract: Tract)

    @Insert
    fun addTract(tract: Tract)

    @Delete
    fun deleteTract(tract: Tract)
}