package com.unicorpdev.ktatract.fragments.collectionList.list

import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.io.File

data class CollectionWithPicture(
    val collection: TractCollection,
    val picture: File?,
    val isModifiable: Boolean = true
) {}

class TractCollectionViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    private val defaultCollection =
        TractCollection(title = "Unclassified", description = "Tracts with no collection")


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

    /***********************************************************************************************
     * Tools - Files
     **********************************************************************************************/

    private fun getCollectionPictureFile(collection: TractCollection): File? {
        return collection.imageFilename?.let { tractRepository.getPictureFile(it) }
    }
}