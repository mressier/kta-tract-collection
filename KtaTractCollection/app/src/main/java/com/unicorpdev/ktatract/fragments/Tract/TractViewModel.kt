package com.unicorpdev.ktatract.fragments.Tract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.Tract
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
        tractRepository.getTractLiveData(id)
    }

    // fun to call to update tract information
    fun loadTract(id: UUID) {
        tractId.value = id
    }

    fun saveTract(tract: Tract) {
        tractRepository.updateTract(tract)
    }

}