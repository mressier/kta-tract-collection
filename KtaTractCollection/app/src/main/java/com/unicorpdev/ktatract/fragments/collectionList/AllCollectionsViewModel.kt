package com.unicorpdev.ktatract.fragments.collectionList

import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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

    fun deleteCollection(collectionId: UUID) {
        GlobalScope.async {
            tractRepository.getCollection(collectionId)?.let { collection ->
                tractRepository.deleteCollection(collection)
            }
        }
    }
}