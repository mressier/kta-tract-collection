package com.unicorpdev.ktatract.fragments.collectionList.spinner

import android.content.Context
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import java.lang.Integer.max
import java.util.*

class CollectionSpinnerViewModel: RepositoryViewModel() {

    var selectedCollectionId: UUID? = null

    lateinit var context: Context

    private var savedCollections: List<TractCollection> = listOf()

    private val defaultCollections by lazy {
        arrayListOf(context.getString(R.string.no_collection_selection))
    }
    private val defaultCollectionPosition = 0


    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun getCollectionIdAtIndex(position: Int): UUID? {
        val position = position - defaultCollections.size
        if (position < 0 || position >= savedCollections.size) { return null }
        return savedCollections[position].id
    }

    fun getPositionForCurrentCollection(): Int {
        return selectedCollectionId?.let { collectionId ->
            val index = savedCollections.indexOfFirst { it.id == collectionId }
            if (index == -1) defaultCollectionPosition else index + defaultCollections.size
        } ?: run { defaultCollectionPosition }
    }

    fun saveCollectionList(collections: List<TractCollection>){
        savedCollections = collections
    }

    fun getCollectionsList(): List<String> {
        return defaultCollections + savedCollections.map { it.title }
    }
}