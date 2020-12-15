package com.unicorpdev.ktatract.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.unicorpdev.ktatract.fragments.picturesList.TractPicturesFragment
import com.unicorpdev.ktatract.fragments.tract.TractFragmentDirections
import com.unicorpdev.ktatract.fragments.tractList.AllTractsFragment
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.home.HomeFragmentDirections
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), AllTractsFragment.Callbacks, TractPicturesFragment.Callbacks {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /***********************************************************************************************
     * Callbacks | TractListFragment
     **********************************************************************************************/

    override fun onTractSelected(tractId: UUID) {
        val action = HomeFragmentDirections.showTract(tractId.toString())
        navController.navigate(action)
    }

    override fun onTractPictureSelected(list: Array<File>, pictureIndex: Int) {
        val paths = list.map { it.name }.toTypedArray()
        val action = HomeFragmentDirections.showTractImages(paths, pictureIndex)
        navController.navigate(action)
    }

    override fun onAboutPageSelected() {
        val action = HomeFragmentDirections.showAboutPage()
        navController.navigate(action)
    }

    /**
     * Callbacks | PicturesListFragment
     */

    override fun onPictureSelected(pictureList: Array<File>,
                                   pictureIndex: Int) {
        val paths = pictureList.map { it.name }.toTypedArray()
        val action = TractFragmentDirections.showTractImages(paths, pictureIndex)
        navController.navigate(action)
    }
}