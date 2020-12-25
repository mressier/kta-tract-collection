package com.unicorpdev.ktatract.fragments.collectionList.list

import android.content.Context
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.io.File
import java.util.*

data class CollectionWithPicture(
    val collection: TractCollection,
    val picture: File?,
    val isModifiable: Boolean = true
) {}

class TractCollectionViewModel: RepositoryViewModel() {

    lateinit var context: Context

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    private val defaultCollection: TractCollection by lazy {
        TractCollection(
            title = context.getString(R.string.my_collection_title),
            description = context.getString(R.string.my_collection_description)
        )
    }


    fun getCollectionsWithPicture(collections: List<TractCollection>): List<CollectionWithPicture> {
        val defaultCollectionWithPicture = CollectionWithPicture(
            defaultCollection,
            null, // get default picture
        false
        )

        val newCollection =
            collections.map { CollectionWithPicture(it, getCollectionPictureFile(it)) }

        return listOf(defaultCollectionWithPicture) + newCollection
    }

    fun isDefaultCollection(collectionId: UUID): Boolean {
        return defaultCollection.id == collectionId
    }

    /***********************************************************************************************
     * Tools - Files
     **********************************************************************************************/

    private fun getCollectionPictureFile(collection: TractCollection): File? {
        return collection.imageFilename?.let { tractRepository.getPictureFile(it) }
    }
}