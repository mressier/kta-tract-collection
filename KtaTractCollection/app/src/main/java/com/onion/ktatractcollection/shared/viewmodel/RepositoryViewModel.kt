package com.onion.ktatractcollection.shared.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onion.ktatractcollection.Database.TractRepository
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture

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