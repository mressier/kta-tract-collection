package com.unicorpdev.ktatract.shared.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unicorpdev.ktatract.Database.TractRepository
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractPicture

open class RepositoryViewModel: ViewModel() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    internal val tractRepository: TractRepository by lazy {
        TractRepository.get()
    }

    /** Live Data **/

    val tracts: LiveData<List<Tract>> = tractRepository.getTracts()
    val pictures: LiveData<List<TractPicture>> = tractRepository.getPictures()

}