package com.unicorpdev.ktatract.fragments.collection

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collectionList.list.CollectionWithPicture
import com.unicorpdev.ktatract.models.TractCollection
import com.unicorpdev.ktatract.shared.extensions.hideKeyboard
import com.unicorpdev.ktatract.shared.fragments.PicturesAddBottomDialogFragment
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PicturesSelectionFragment
import com.unicorpdev.ktatract.shared.tools.TextChangedWatcher
import kotlinx.android.synthetic.main.fragment_collection_edit.*
import java.io.File
import java.util.*

class CollectionEditFragment : Fragment(), PicturesAddBottomDialogFragment.Callbacks {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel by viewModels<CollectionEditViewModel>()

    private lateinit var collection: TractCollection

    private val bottomSheetDialogFragment: PicturesAddBottomDialogFragment by lazy {
        val bottomSheetDialog = PicturesAddBottomDialogFragment.newInstance()
        bottomSheetDialog.callbacks = this
        bottomSheetDialog
    }

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        collection = TractCollection()

        arguments?.let {
            val args = CollectionEditFragmentArgs.fromBundle(it)
            args.collectionId.let { collectionId ->
                val id = UUID.fromString(collectionId)
                collection = TractCollection(id = id)
                viewModel.loadCollection(id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
    }

    override fun onStop() {
        super.onStop()
        println(collection)
        viewModel.saveCollection(collection)
    }

    /***********************************************************************************************
     * Menu
     **********************************************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_item -> {
                requireActivity().hideKeyboard()
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /***********************************************************************************************
     * Update View
     **********************************************************************************************/

    private fun updateView(collectionWithPicture: CollectionWithPicture) {
        updateTitle(collectionWithPicture.collection.title)
        updateDescription(collectionWithPicture.collection.description)
        updateImageView(collectionWithPicture.picture)
    }

    private fun updateTitle(string: String) {
        titleTextInputLayout.editText?.setText(string)
    }

    private fun updateDescription(string: String) {
        descriptionTextInputLayout.editText?.setText(string)
    }

    private fun updateImageView(image: File?) {
        Glide.with(collectionImageView)
            .load(image)
            .circleCrop()
            .placeholder(R.drawable.ic_no_tract_photo)
            .into(collectionImageView)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupObservers() {
        viewModel.collection.observe(viewLifecycleOwner) { collection ->
            collection?.let {
                this.collection = collection
                val collectionWithPicture = viewModel.getCollectionWithPicture(it)
                println("Get collection $collection")
                updateView(collectionWithPicture)
            }
        }
    }

    private fun setupListeners() {
        setupTitleListener()
        setupDescriptionListener()
        setupImageViewListener()
    }

    private fun setupTitleListener() {
        val titleWatcher = TextChangedWatcher() { collection.title = it }
        titleTextInputLayout.editText?.addTextChangedListener(titleWatcher)
    }

    private fun setupDescriptionListener() {
        val descriptionWatcher = TextChangedWatcher() { collection.description = it }
        descriptionTextInputLayout.editText?.addTextChangedListener(descriptionWatcher)
    }

    private fun setupImageViewListener() {
        collectionImageView.setOnClickListener {
            bottomSheetDialogFragment.show(
                requireFragmentManager(),
                PicturesAddBottomDialogFragment.TAG
            )
        }
    }

    override fun onPicturesSelected(pictures: List<Uri>) {
        val picture = pictures.firstOrNull() ?: return

        Log.d(TAG, "Get picture for collection: ${picture.lastPathSegment}")

        collection.imageFilename = picture.lastPathSegment
        viewModel.saveCollection(collection)

        bottomSheetDialogFragment.dismiss()
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {

        private val TAG = CollectionEditFragment::class.simpleName ?: "Default"

        @JvmStatic
        fun newInstance() = CollectionEditFragment()
    }

}