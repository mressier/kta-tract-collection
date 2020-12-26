package com.unicorpdev.ktatract.fragments.collectionList.spinner

import android.content.Context
import com.unicorpdev.ktatract.database.KtaTractLocalStorage
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.util.*

class CollectionSpinnerViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    lateinit var context: Context

    /** Collection **/

    var selectedCollectionId: UUID = KtaTractLocalStorage.getInstance().defaultCollectionId

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