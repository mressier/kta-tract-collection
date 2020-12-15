package com.unicorpdev.ktatract.shared.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.database.TractRepository
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.models.TractPicture

open class RepositoryViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    internal val tractRepository: TractRepository by lazy {
        TractRepository.get()
    }

    /** Live Data **/

    val tracts: LiveData<List<Tract>> by lazy {
        tractRepository.getTractsLiveData()
    }

    val pictures: LiveData<List<TractPicture>> by lazy {
        tractRepository.getPicturesLiveData()
    }

    val collections: LiveData<List<TractCollection>> by lazy {
        tractRepository.getCollectionsLiveData()
    }

}