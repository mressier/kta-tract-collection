package com.onion.ktatractcollection.Fragments.Tract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import java.util.*

class TractViewModel : ViewModel() {

    /**
     * Properties
     */

    private val tractRepository = TractRepository.get()

    /** One tract **/
    private var tractId = MutableLiveData<UUID>()

    // Item to observe
    var tract: LiveData<Tract?> = Transformations.switchMap(tractId) { id ->
        tractRepository.getTract(id)
    }

    // fun to call to update tract information
    fun loadTract(id: UUID) {
        tractId.value = id
    }

    fun saveTract(tract: Tract) {
        tractRepository.updateTract(tract)
    }
}