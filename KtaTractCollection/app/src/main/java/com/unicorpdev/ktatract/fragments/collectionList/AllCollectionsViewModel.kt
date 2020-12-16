package com.unicorpdev.ktatract.fragments.collectionList

import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.util.*

class AllCollectionsViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun createCollection(): UUID {
        val collection = TractCollection()
        tractRepository.addCollection(collection)
        return collection.id
    }
}