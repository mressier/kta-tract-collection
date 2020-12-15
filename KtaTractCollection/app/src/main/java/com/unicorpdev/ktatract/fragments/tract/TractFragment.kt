package com.unicorpdev.ktatract.fragments.tract

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.unicorpdev.ktatract.fragments.fab.FabImageMenuFragment
import com.unicorpdev.ktatract.fragments.tract.TractTabLayout.TractDetailsTabsFragment
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.extensions.hideKeyboard
import java.util.*

class TractFragment : Fragment(), FabImageMenuFragment.Callbacks {

    /**
     * Properties
     */
    private lateinit var tabsFragment: TractDetailsTabsFragment
    private lateinit var detailsFragment: TractDetailsFragment
    private lateinit var fabFragment: FabImageMenuFragment

    private lateinit var tractId: UUID

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            val args = TractFragmentArgs.fromBundle(it)
            args.tractId.let { tractId ->
                this.tractId = UUID.fromString(tractId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract, container, false)
        setupOutlets(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "Set tract id on sub fragments $tractId")
        tabsFragment.setTract(tractId)
        detailsFragment.setTract(tractId)
        fabFragment.setTract(tractId)
        fabFragment.setShouldShowMultipleImportButton(false)
        setupListeners()

        ViewCompat.setTranslationZ(view, 100f)
    }

    /**
     * Menu
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_tract_item -> {
                requireActivity().hideKeyboard()
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Setup
     */

    private fun setupOutlets(view: View) {
        detailsFragment =
            childFragmentManager.findFragmentById(R.id.tract_detail_fragment) as TractDetailsFragment
        tabsFragment =
            childFragmentManager.findFragmentById(R.id.tract_tab_fragment) as TractDetailsTabsFragment
        fabFragment =
            childFragmentManager.findFragmentById(R.id.fabFragment) as FabImageMenuFragment
    }

    private fun setupListeners() {
        fabFragment.callbacks = this
    }

    override fun onTractSaved(tractId: UUID) {
        Log.v(TAG, "Tract saved $tractId")
    }

    override fun onTractsSaved(tractIds: Array<UUID>) {
        Log.e(TAG, "Not implemented - no multiple import")
    }

    /**
     * Companion
     */
    companion object {
        private const val TAG = "TractFragment"
    }
}
