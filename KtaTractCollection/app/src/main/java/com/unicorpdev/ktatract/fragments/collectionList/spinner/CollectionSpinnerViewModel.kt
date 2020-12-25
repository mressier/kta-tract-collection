package com.unicorpdev.ktatract.fragments.collectionList.spinner

import android.content.Context
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.database.TractRepository.Companion.DEFAULT_COLLECTION_ID
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.lang.Integer.max
import java.util.*

class CollectionSpinnerViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    lateinit var context: Context

    /** Collection **/

    var selectedCollectionId: UUID = DEFAULT_COLLECTION_ID

    var savedCollections: List<TractCollection> = listOf()

    val collectionsNames: List<String>
        get() = savedCollections.map { it.title }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun getCollectionIdAtIndex(position: Int): UUID {
        return savedCollections[position].id
    }

    fun getPositionForCurrentCollection(): Int {
        return savedCollections.indexOfFirst { it.id == selectedCollectionId }
    }
}