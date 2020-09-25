package com.onion.ktatractcollection.Fragments.Fab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.onion.ktatractcollection.R


/**
 * A simple [Fragment] subclass.
 */
class FabImageMenuFragment : Fragment() {

    /**
     * Properties
     */

    /* Outlets */

    private lateinit var newTractButton: FloatingActionButton
    private lateinit var galleryButton: FloatingActionButton
    private lateinit var galleryText: TextView
    private lateinit var cameraButton: FloatingActionButton
    private lateinit var cameraText: TextView
    private lateinit var backgroundView: ImageView

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation

    /* View Models */

    private val imageMenuViewModel: FabImageMenuViewModel by lazy {
        ViewModelProvider(this).get(FabImageMenuViewModel::class.java)
    }

    /**
     * View Life Cycle
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fab_image_menu, container, false)
        setupView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    /**
     * Methods
     */

    private fun showMenu() {
        imageMenuViewModel.isMenuVisible = true

        newTractButton.startAnimation(fabClock)

        galleryButton.show()
        galleryText.visibility = View.VISIBLE

        cameraButton.show()
        cameraText.visibility = View.VISIBLE

        backgroundView.isClickable = true
        backgroundView.startAnimation(fabOpen)
    }

    private fun hideMenu() {
        imageMenuViewModel.isMenuVisible = false

        newTractButton.startAnimation(fabAnticlock)

        galleryButton.hide()
        galleryText.visibility = View.GONE

        cameraButton.hide()
        cameraText.visibility = View.GONE

        backgroundView.isClickable = false
        backgroundView.startAnimation(fabClose)
    }

    /**
     * Setup
     */

    private fun setupView(view: View) {
        newTractButton = view.findViewById(R.id.add_button)
        galleryButton = view.findViewById(R.id.gallery_button)
        galleryText = view.findViewById(R.id.gallery_text)
        cameraButton = view.findViewById(R.id.camera_button)
        cameraText = view.findViewById(R.id.camera_text)
        backgroundView = view.findViewById(R.id.background_view)

        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fabClock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);

        if (imageMenuViewModel.isMenuVisible) { showMenu() } else { hideMenu() }
    }

    private fun setupListeners() {
        newTractButton.setOnClickListener {
            if (imageMenuViewModel.isMenuVisible) { hideMenu() } else { showMenu() }
        }

        backgroundView.setOnClickListener {
            hideMenu()
        }
    }
}