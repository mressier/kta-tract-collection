package com.onion.ktatractcollection.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.onion.ktatractcollection.Models.TractPicture
import java.util.*

@Dao
interface PictureDao {
    /**
     * Pictures
     */
    @Query("SELECT * FROM tractpicture WHERE tractId=(:tractId)")
    fun getPicturesForTract(tractId: UUID): LiveData<List<TractPicture>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPicture(picture: TractPicture)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun addPictures(pictures: List<TractPicture>)

    @Delete
    fun deletePicture(picture: TractPicture)

    @Query("DELETE FROM tractpicture WHERE tractId=(:tractId)")
    fun deletePicturesForTract(tractId: UUID)
}