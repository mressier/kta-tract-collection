package com.unicorpdev.ktatract.fragments.tractList

import com.unicorpdev.ktatract.shared.viewmodel.RepositoryViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class AllTractsViewModel: RepositoryViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    var collectionId: UUID? = null

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun deleteTract(tractId: UUID) {
        GlobalScope.async {
            val tract = tractRepository.getTract(tractId)
            tract?.let { tractRepository.deleteTract(it) }
        }
    }

    fun updateTractIsFavorite(tractId: UUID, isFavorite: Boolean) {
        GlobalScope.async {
            val tract = tractRepository.getTract(tractId)

            tract?.let {
                it.isFavorite = isFavorite
                tractRepository.updateTract(it)
            }
        }
    }
}