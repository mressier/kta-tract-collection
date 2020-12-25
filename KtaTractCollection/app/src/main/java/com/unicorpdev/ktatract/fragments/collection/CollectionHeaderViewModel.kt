package com.unicorpdev.ktatract.fragments.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.fragments.collectionList.list.CollectionWithPicture
import com.unicorpdev.ktatract.models.TractCollection
import java.io.File
import java.util.*

class CollectionHeaderViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val repository: TractRepository by lazy {
        TractRepository.get()
    }

    /** One Collection **/
    private var collectionId = MutableLiveData<UUID?>()

    // Item to observe
    var collection: LiveData<TractCollection?> = Transformations.switchMap(collectionId) { id ->
        id?.let { repository.getCollectionLiveData(it) }
    }

    var savedCollection: TractCollection? = null

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    // fun to call to update collection information
    fun loadCollection(id: UUID?) {
        collectionId.value = id
    }

    fun getCollectionPicture(collection: TractCollection): File? {
        return collection.imageFilename?.let { repository.getPictureFile(it) }
    }

}