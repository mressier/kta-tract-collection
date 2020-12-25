package com.unicorpdev.ktatract.fragments.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractCollection
import kotlinx.android.synthetic.main.fragment_about_page.*
import kotlinx.android.synthetic.main.fragment_collection_header.*
import kotlinx.android.synthetic.main.fragment_collection_header.descriptionTextView
import kotlinx.android.synthetic.main.fragment_collection_header.titleTextView
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionHeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionHeaderFragment : Fragment() {

    private val viewModel by viewModels<CollectionHeaderViewModel>()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun loadCollection(collectionId: UUID?) {
        viewModel.loadCollection(collectionId)
    }

    /***********************************************************************************************
     * Update
     **********************************************************************************************/

    private fun updateUI(collection: TractCollection) {
        updateTitleTextView(collection.title)
        updateDescriptionTextView(collection.description)
        updateImageView(viewModel.getCollectionPicture(collection))
    }

    private fun updateTitleTextView(title: String) {
        titleTextView.text = title
    }

    private fun updateDescriptionTextView(description: String) {
        descriptionTextView.visibility =
            if (description.isEmpty()) View.GONE else View.VISIBLE
        descriptionTextView.text = description
    }

    private fun updateImageView(file: File?) {
        Glide.with(collectionImageView)
            .load(file)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(collectionImageView)
    }

    private fun updateUIDefault() {
        updateTitleTextView(getString(R.string.my_collection_title))
        updateDescriptionTextView(getString(R.string.my_collection_description))
        updateImageView(null)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupObservers() {
        viewModel.collection.observe(viewLifecycleOwner) { collection ->
            Log.d(TAG, "Receive collection information : $collection")
            collection?.let { updateUI(it) } ?: run { updateUIDefault() }
        }
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        private val TAG = CollectionHeaderFragment::class.simpleName ?: "Default"

        @JvmStatic
        fun newInstance() = CollectionHeaderFragment()
    }
}