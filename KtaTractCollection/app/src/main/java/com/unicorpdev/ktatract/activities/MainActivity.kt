package com.unicorpdev.ktatract.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collection.CollectionHeaderFragment
import com.unicorpdev.ktatract.fragments.collectionList.AllCollectionsFragment
import com.unicorpdev.ktatract.fragments.home.HomeFragment
import com.unicorpdev.ktatract.fragments.home.HomeFragmentDirections
import com.unicorpdev.ktatract.fragments.picturesList.TractPicturesFragment
import com.unicorpdev.ktatract.fragments.tract.TractFragmentDirections
import com.unicorpdev.ktatract.fragments.tractList.AllTractsFragment
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.extensions.shortString
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(),
    AllTractsFragment.Callbacks,
    AllCollectionsFragment.Callbacks,
    TractPicturesFragment.Callbacks,
    CollectionHeaderFragment.Callbacks
{

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

    override fun onStart() {
        super.onStart()
        setupNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isNotHome = destination.id != R.id.homeFragment
            supportActionBar?.setDisplayHomeAsUpEnabled(isNotHome)
        }
    }

    /***********************************************************************************************
     * Callbacks | TractListFragment
     **********************************************************************************************/

    override fun onEditTract(tractId: UUID) {
        val action = HomeFragmentDirections.showTractEdit(tractId.toString())
        navController.navigate(action)
    }

    override fun onTractSelected(tract: TractWithPicture) {
        onTractPictureSelected(tract, 0)
    }

    override fun onTractPictureSelected(tract: TractWithPicture, pictureIndex: Int) {
        val list = tract.pictures.map { tract.picturesFile[it.id] ?: error("") }.toTypedArray()
        val paths = list.map { it.name }.toTypedArray()
        val action = HomeFragmentDirections.showTractImages(
            paths, pictureIndex, tract.tract.author, tract.tract.comment,
            tract.tract.dating?.let {
                getString(R.string.tract_dating_from).format(tract.tract.dating?.shortString)
            }
        )
        navController.navigate(action)
    }

    override fun onAboutPageSelected() {
        val action = HomeFragmentDirections.showAboutPage()
        navController.navigate(action)
    }

    /***********************************************************************************************
     * Callbacks | PicturesListFragment
     **********************************************************************************************/

    override fun onPictureSelected(
        tract: TractWithPicture,
        pictureIndex: Int
    ) {
        Log.d("TAG", "tract pictures: ${tract.pictures.map { it.id }}")
        Log.d("TAG", "tract pictures: ${tract.picturesFile}")
        val list = tract.pictures.map {
            tract.picturesFile[it.id] ?: error("Invalid id ${it.id}")
        }.toTypedArray()
        val paths = list.map { it.name }.toTypedArray()
        val action = HomeFragmentDirections.showTractImages(
            paths, pictureIndex, tract.tract.author, tract.tract.comment,
            tract.tract.dating?.let {
                getString(R.string.tract_dating_from).format(tract.tract.dating?.shortString)
            }
        )
        navController.navigate(action)
    }

    /***********************************************************************************************
     * Callbacks | CollectionListFragment
     **********************************************************************************************/

    override fun onSelectCollection(collectionId: UUID) {
        val action = HomeFragmentDirections.showCollectionContent(collectionId.toString())
        navController.navigate(action)
    }

    override fun onCreateCollection(collectionId: UUID) {
        val action = HomeFragmentDirections.showCollectionEdit(collectionId.toString())
        navController.navigate(action)
    }

    override fun onUpdateCollection(collectionId: UUID) {
        val action = HomeFragmentDirections.showCollectionEdit(collectionId.toString())
        navController.navigate(action)
    }
}