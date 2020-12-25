package com.unicorpdev.ktatract.fragments.collectionList.list

import android.content.Context
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.io.File
import java.util.*

data class CollectionWithPicture(
    val collection: TractCollection,
    val picture: File?
) {}

class TractCollectionViewModel: RepositoryViewModel() {

    lateinit var context: Context

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun getCollectionsWithPicture(collections: List<TractCollection>): List<CollectionWithPicture> {
        return collections.map { CollectionWithPicture(it, getCollectionPictureFile(it)) }
    }

    /***********************************************************************************************
     * Tools - Files
     **********************************************************************************************/

    private fun getCollectionPictureFile(collection: TractCollection): File? {
        return collection.imageFilename?.let { tractRepository.getPictureFile(it) }
    }
}