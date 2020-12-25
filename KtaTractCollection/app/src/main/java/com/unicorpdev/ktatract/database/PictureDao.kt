package com.unicorpdev.ktatract.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture
import java.util.*

@Dao
interface PictureDao {

    /***********************************************************************************************
     * Query
     **********************************************************************************************/
    
    @Query("SELECT * FROM tractpicture")
    fun getPicturesLiveData(): LiveData<List<TractPicture>>

    @Query("SELECT * FROM tractpicture WHERE tractId=(:tractId)")
    fun getPicturesForTractLiveData(tractId: UUID): LiveData<List<TractPicture>>

    @Query("SELECT * FROM tractpicture WHERE tractId IN ( SELECT id FROM tract WHERE collectionId=(:collectionId) )")
    fun getPicturesForCollectionLiveData(collectionId: UUID): LiveData<List<TractPicture>>

    @Query("SELECT * FROM tractpicture WHERE tractId IN ( SELECT id FROM tract WHERE collectionId IS NULL)")
    fun getPicturesForTractWithoutCollectionLiveData(): LiveData<List<TractPicture>>

    /***********************************************************************************************
     * Query
     **********************************************************************************************/

    @Query("SELECT * FROM tractpicture")
    fun getPictures(): List<TractPicture>

    @Query("SELECT * FROM tractpicture WHERE tractId=(:tractId)")
    fun getPicturesForTract(tractId: UUID): List<TractPicture>

    @Query("SELECT * FROM tractpicture WHERE tractId IN ( SELECT id FROM tract WHERE collectionId=(:collectionId) )")
    fun getPicturesForCollection(collectionId: UUID): List<TractPicture>

    @Query("SELECT * FROM tractpicture WHERE tractId IN ( SELECT id FROM tract WHERE collectionId IS NULL)")
    fun getPicturesForTractWithoutCollection(): List<TractPicture>

    /***********************************************************************************************
     * Insert
     **********************************************************************************************/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPicture(picture: TractPicture)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun addPictures(pictures: List<TractPicture>)

    /***********************************************************************************************
     * Delete
     **********************************************************************************************/

    @Delete
    fun deletePicture(picture: TractPicture)

    @Delete
    fun deletePictures(pictures: List<TractPicture>)

    @Query("DELETE FROM tractpicture WHERE tractId=(:tractId)")
    fun deletePicturesForTract(tractId: UUID)

    @Query("DELETE FROM tractpicture WHERE tractId in (SELECT id FROM tract WHERE collectionId=(:collectionId))")
    fun deletePictureForCollection(collectionId: UUID)
}