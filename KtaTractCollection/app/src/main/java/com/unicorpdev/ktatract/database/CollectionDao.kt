package com.unicorpdev.ktatract.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unicorpdev.ktatract.models.TractCollection
import java.util.*

@Dao
interface CollectionDao {
    
    /***********************************************************************************************
     * Query - Live Data
     **********************************************************************************************/

    @Query("SELECT * FROM tractcollection")
    fun getCollectionsLiveData(): LiveData<List<TractCollection>>

    @Query("SELECT * FROM tractcollection WHERE id=(:collectionId)")
    fun getCollectionLiveData(collectionId: UUID): LiveData<TractCollection?>

    /***********************************************************************************************
     * Query - Raw Data
     **********************************************************************************************/
    
    @Query("SELECT * FROM tractcollection")
    fun getCollections(): List<TractCollection>

    @Query("SELECT * FROM tractcollection WHERE id=(:collectionId)")
    fun getCollection(collectionId: UUID): TractCollection?

    /***********************************************************************************************
     * Insert
     **********************************************************************************************/
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCollection(collection: TractCollection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCollections(collections: List<TractCollection>)

    /***********************************************************************************************
     * Update
     **********************************************************************************************/

    @Update
    fun updateCollection(collection: TractCollection)

    /***********************************************************************************************
     * Delete
     **********************************************************************************************/

    @Delete
    fun deleteCollection(collection: TractCollection)
}