package com.unicorpdev.ktatract.fragments.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.fragments.collectionList.list.CollectionWithPicture
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import java.util.*

class CollectionEditViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val tractRepository = TractRepository.get()

    /** Collection **/

    private var collectionId = MutableLiveData<UUID>()

    // Item to observe
    var collection: LiveData<TractCollection?> = Transformations.switchMap(collectionId) { id ->
        tractRepository.getCollectionLiveData(id)
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun loadCollection(id: UUID) {
        collectionId.value = id
    }

    fun saveCollection(collection: TractCollection) {
        tractRepository.updateCollection(collection)
    }

    fun getCollectionWithPicture(collection: TractCollection): CollectionWithPicture {
        val file = collection.imageFilename?.let { tractRepository.getPictureFile(it) }
        return CollectionWithPicture(collection, file)
    }
}