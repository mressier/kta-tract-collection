package com.unicorpdev.ktatract.fragments.fab

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.fab.FabImageMenuViewModel.PictureSelectionOption
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PictureSelectionMethod
import com.unicorpdev.ktatract.shared.fragments.picturesSelection.PicturesSelectionable
import kotlinx.android.synthetic.main.fragment_fab_image_menu.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class FabImageMenuFragment: PicturesSelectionable() {

    interface Callbacks {
        fun onTractSaved(tractId: UUID)
        fun onTractsSaved(tractIds: Array<UUID>)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var fabClock: Animation
    private lateinit var fabAntiClock: Animation

    private val imageMenuViewModel by viewModels<FabImageMenuViewModel>()

    var callbacks: Callbacks? = null

    /***********************************************************************************************
     * Callbacks
     **********************************************************************************************/

    private val interceptBackPressed: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (imageMenuViewModel.isMenuVisible) { hideMenu(true) }
            }
        }
    }

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(interceptBackPressed)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAnimations()
        return inflater.inflate(R.layout.fragment_fab_image_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupButtonsTexts()

        if (imageMenuViewModel.isMenuVisible) {
            showMenu(animated = false)
        } else {
            hideMenu(animated = false)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        callbacks = null
        revokeCameraPermission()
        super.onDetach()
    }
    
    /***********************************************************************************************
     * PicturesSelectionable
     **********************************************************************************************/

    override fun onPictureSelected(pictures: List<Uri>) {
        when (imageMenuViewModel.pictureSelectionOption) {
            PictureSelectionOption.CREATE_ONE_TRACT -> savePicturesForTract(pictures)
            PictureSelectionOption.CREATE_MULTIPLE_TRACTS -> savePicturesAsTracts(pictures)
        }
    }
    /**
     * Methods
     */

    fun setTract(tractId: UUID?) {
        imageMenuViewModel.tractId = tractId
    }

    fun setCollection(collectionId: UUID?) {
        imageMenuViewModel.collectionId = collectionId
    }

    fun setShouldShowMultipleImportButton(shouldShowMultipleImportButton: Boolean) {
        imageMenuViewModel.shouldShowMultipleImport = shouldShowMultipleImportButton
    }
    
    /***********************************************************************************************
     * Tools
     **********************************************************************************************/

    private fun savePicturesForTract(pictures: List<Uri>) {
        imageMenuViewModel.savePicturesFile(pictures)
        imageMenuViewModel.tractId?.let {
            callbacks?.onTractSaved(it)
        }
        hideMenu()
    }

    private fun savePicturesAsTracts(pictures: List<Uri>) {
        val tracts = pictures.map { uri ->
            val tract = imageMenuViewModel.generateTract()
            imageMenuViewModel.savePicturesFile(listOf(uri))
            tract
        }

        callbacks?.onTractsSaved(tracts.toTypedArray())
        hideMenu()
    }

    /***********************************************************************************************
     * Menu
     **********************************************************************************************/
    
    private fun showMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = true
        interceptBackPressed.isEnabled = true

        fabClock.duration = duration
        newTractButton.startAnimation(fabClock)

        galleryButton.show()
        galleryText.visibility = View.VISIBLE

        cameraButton.show()
        cameraText.visibility = View.VISIBLE

        if (imageMenuViewModel.shouldShowMultipleImport) {
            multipleImportButton.show()
            multipleImportText.visibility = View.VISIBLE
        } else {
            multipleImportButton.hide()
            multipleImportText.visibility = View.GONE
        }

        backgroundView.isClickable = true

        backgroundView.animate().alpha(0.7F).duration = duration
    }

    private fun hideMenu(animated: Boolean = true) {
        val duration: Long = if (animated) { 300 } else { 0 }

        imageMenuViewModel.isMenuVisible = false
        interceptBackPressed.isEnabled = false

        fabAntiClock.duration = duration
        newTractButton.startAnimation(fabAntiClock)

        galleryButton.hide()
        galleryText.visibility = View.GONE

        cameraButton.hide()
        cameraText.visibility = View.GONE

        multipleImportButton.hide()
        multipleImportText.visibility = View.GONE

        backgroundView.isClickable = false

        backgroundView.animate().alpha(0F).duration = duration
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupAnimations() {
        fabClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fabAntiClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);
    }

    private fun setupListeners() {
        newTractButton.setOnClickListener {
            if (imageMenuViewModel.isMenuVisible) {
                hideMenu(animated = true)
            } else {
                showMenu(animated = true)
            }
        }

        backgroundView.setOnClickListener { hideMenu() }

        cameraButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_ONE_PICTURE)
            imageMenuViewModel.pictureSelectionOption =
                PictureSelectionOption.CREATE_ONE_TRACT
            startPictureSelection(PictureSelectionMethod.CAMERA)
        }

        galleryButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_ONE_GALLERY)
            imageMenuViewModel.pictureSelectionOption = PictureSelectionOption.CREATE_ONE_TRACT
            startPictureSelection(PictureSelectionMethod.GALLERY)
        }

        multipleImportButton.setOnClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_MULTIPLE)
            imageMenuViewModel.pictureSelectionOption =
                PictureSelectionOption.CREATE_MULTIPLE_TRACTS
            startPictureSelection(PictureSelectionMethod.GALLERY)
        }
    }

    private fun setupButtonsTexts() {
        val galleryButtonText = if (imageMenuViewModel.tractId == null)
            R.string.fab_btn_import_tract_from_gallery
        else R.string.fab_btn_import_image_from_gallery

        val cameraButtonText = if (imageMenuViewModel.tractId == null)
            R.string.fab_btn_import_tract_take_picture
        else R.string.fab_btn_import_image_take_picture

        multipleImportText.text = getString(R.string.fab_btn_multiple_import)
        galleryText.text = getString(galleryButtonText)
        cameraText.text = getString(cameraButtonText)
    }

    /**
     * Companion
     */
    companion object {
        private val TAG = FabImageMenuFragment::class.simpleName ?: "Default"
    }
}
