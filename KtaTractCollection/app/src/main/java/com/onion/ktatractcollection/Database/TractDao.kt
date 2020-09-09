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

    @Insert
    fun addTract(tract: Tract)

    @Delete
    fun deleteTract(tract: Tract)

    /**
     * Pictures
     */
    @Query("SELECT * FROM tractpicture WHERE tractId=(:tractId)")
    fun getPicturesForTract(tractId: UUID): LiveData<List<TractPicture>>

    @Insert
    fun addPicture(picture: TractPicture)

    @Delete
    fun deletePicture(picture: TractPicture)

    @Query("DELETE FROM tractpicture WHERE tractId=(:tractId)")
    fun deletePicturesForTract(tractId: UUID)

}