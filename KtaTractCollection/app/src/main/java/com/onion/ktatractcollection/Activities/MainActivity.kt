package com.onion.ktatractcollection.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.onion.ktatractcollection.Fragments.PicturesList.PicturesListFragment
import com.onion.ktatractcollection.Fragments.Tract.TractFragmentDirections
import com.onion.ktatractcollection.Fragments.TractList.TractListFragment
import com.onion.ktatractcollection.Fragments.TractList.TractListFragmentDirections
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.R
import java.util.*

class MainActivity : AppCompatActivity(), TractListFragment.Callbacks, PicturesListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Callbacks | TractListFragment
     */

    override fun onTractSelected(tractId: UUID) {
        val action = TractListFragmentDirections.showTract(tractId.toString())
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun onTractPictureSelected(list: Array<TractPicture>, pictureIndex: Int) {
        val paths = list.map { it.photoFilename }.toTypedArray()
        val action = TractListFragmentDirections.showTractImages(paths, pictureIndex)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }


    /**
     * Callbacks | PicturesListFragment
     */

    override fun onPictureListSelected(list: Array<TractPicture>,
                                       pictureIndex: Int) {
        val paths = list.map { it.photoFilename }.toTypedArray()
        val action = TractFragmentDirections.showTractImages(paths, pictureIndex)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}