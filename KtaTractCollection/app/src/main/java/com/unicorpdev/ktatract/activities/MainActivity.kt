package com.unicorpdev.ktatract.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.unicorpdev.ktatract.fragments.PicturesList.TractPicturesFragment
import com.unicorpdev.ktatract.fragments.Tract.TractFragmentDirections
import com.unicorpdev.ktatract.fragments.TractList.AllTractsFragment
import com.unicorpdev.ktatract.fragments.TractList.AllTractsFragmentDirections
import com.unicorpdev.ktatract.R
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), AllTractsFragment.Callbacks, TractPicturesFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Callbacks | TractListFragment
     */

    override fun onTractSelected(tractId: UUID) {
        val action = AllTractsFragmentDirections.showTract(tractId.toString())
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun onTractPictureSelected(list: Array<File>, pictureIndex: Int) {
        val paths = list.map { it.name }.toTypedArray()
        val action = AllTractsFragmentDirections.showTractImages(paths, pictureIndex)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun onAboutPageSelected() {
        val action = AllTractsFragmentDirections.showAboutPage()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    /**
     * Callbacks | PicturesListFragment
     */

    override fun onPictureSelected(pictureList: Array<File>,
                                   pictureIndex: Int) {
        val paths = pictureList.map { it.name }.toTypedArray()
        val action = TractFragmentDirections.showTractImages(paths, pictureIndex)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}