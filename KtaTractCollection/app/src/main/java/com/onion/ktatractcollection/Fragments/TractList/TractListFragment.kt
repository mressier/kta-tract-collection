package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.onion.ktatractcollection.Fragments.Fab.FabImageMenuFragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParametersViewModel
import com.onion.ktatractcollection.Fragments.TractList.header.TractListHeaderFragment
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Models.Tract
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.shared.dialogs.SortListCallbacks
import com.onion.ktatractcollection.shared.dialogs.TractActionCallback
import com.onion.ktatractcollection.shared.dialogs.showSortListDialog
import com.onion.ktatractcollection.shared.dialogs.showTractActionDialog
import kotlinx.android.synthetic.main.fragment_tract_list.*
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class TractListFragment :
    Fragment(),
    TractListCallbacks,
    FabImageMenuFragment.Callbacks,
    TractListHeaderFragment.Callbacks
{

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTractSelected(tractId: UUID)
        fun onTractPictureSelected(list: Array<TractPicture>, pictureIndex: Int)
    }

    /**
     * Properties
     */

    private var callbacks: Callbacks? = null
    private val tractListViewModel: TractListViewModel by lazy {
        ViewModelProvider(this).get(TractListViewModel::class.java)
    }

    private val parametersViewModel: TractListParametersViewModel by lazy {
        ViewModelProvider(this).get(TractListParametersViewModel::class.java)
    }

    private lateinit var fabFragment: FabImageMenuFragment
    private lateinit var headerFragment: TractListHeaderFragment

    private lateinit var tractAdapter: TractListAdapter
    private lateinit var tractLayout: GridLayoutManager
    private var currentTracts: List<Tract> = listOf()

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tract_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupButtonListener()

        if (savedInstanceState == null) {
            setupViewModelObserver()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerViewState()
    }

    /**
     * Menu
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sort_list -> {
                showSortTractListDialog()
                true
            }
            R.id.export_collection -> {
                tractListViewModel.exportCollection()
                true
            }
            R.id.import_collection -> {
                tractListViewModel.importCollection()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Update
     */
    private fun updateUI() {
        val tracts = parametersViewModel.getDisplayedTracts(currentTracts)
        tractListViewModel.saveAsTractsWithPicture(tracts)

        updateTractListLayout()
        updateTractListContent(tractListViewModel.tractsWithPicture)
    }

    private fun updateTractListContent(
        tractListItems: List<TractWithPicture>
    ) {
        val previousTracts = tractAdapter.currentList

        tractAdapter.submitList(tractListItems)
        tractAdapter.notifyDataSetChanged()

        updateNoTractImageVisibility(previousTracts, tractListItems)
    }

    private fun updateNoTractImageVisibility(previousTracts: List<TractWithPicture>,
                                             newTracts: List<TractWithPicture>) {
        if (previousTracts.isNotEmpty() && newTracts.isNotEmpty()) { return }

        val noTractVisibility =
            if (newTracts.isNotEmpty()) { View.GONE } else { View.VISIBLE }
        noTractImageView.visibility = noTractVisibility
        noTractText.visibility = noTractVisibility

        tractListLayout.visibility = if (newTracts.isEmpty()) { View.GONE } else { View.VISIBLE }
    }

    private fun saveRecyclerViewState() {
        tractListViewModel.recyclerViewState = tractRecyclerView.layoutManager?.onSaveInstanceState()

        Log.d(TAG, "Save recycler view state ${tractListViewModel.recyclerViewState}")
    }

    private fun restoreRecyclerViewState() {
        Log.d(TAG, "Restore recycler view state ${tractListViewModel.recyclerViewState}?")

        tractListViewModel.recyclerViewState?.let {
            tractRecyclerView.layoutManager?.onRestoreInstanceState(it)
        }

        tractListViewModel.recyclerViewState = null
    }

    private fun updateTractListLayout() {
        tractLayout.spanCount = parametersViewModel.displayMode.spanCount

        tractAdapter.parameters = parametersViewModel.parameters
        tractAdapter.notifyItemRangeChanged(0, tractAdapter.itemCount)
    }

    /**
     * Setup
     */

    private fun setupView() {
        setupRecyclerView()
        setupFragments()
        setupNoTractView()
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "Setup Recycler view !")
        tractAdapter = TractListAdapter(this)
        tractLayout = GridLayoutManager(context, 1)

        tractRecyclerView.adapter = tractAdapter
        tractRecyclerView.layoutManager = tractLayout
    }

    private fun setupFragments() {
        fabFragment =
            childFragmentManager.findFragmentById(R.id.fabFragment) as FabImageMenuFragment
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as TractListHeaderFragment
    }

    private fun setupNoTractView() {
        noTractImageView.visibility = View.GONE
        noTractImageView.jumpDrawablesToCurrentState()
        noTractText.visibility = View.GONE
        noTractText.jumpDrawablesToCurrentState()
    }

    /**
     * Listener
     */

    private fun setupViewModelObserver() {
        tractListViewModel.tracts.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                items.let {
                    Log.i(TAG, "Got items ${it.size}")
                    currentTracts = items
                    setupImagesForTracts()
                }
            }
        )
    }

    private fun setupImagesForTracts() {
        val tracts = parametersViewModel.getDisplayedTracts(currentTracts)
        var count = tracts.size
        tractListViewModel.saveAsTractsWithPicture(tracts)

        tracts.map { it.id }.forEach { tractId ->
            tractListViewModel.getPictures(tractId).observe(
                owner = viewLifecycleOwner,
                onChanged = { items ->
                    tractListViewModel.addPicturesToTractItem(tractId, items)
                    count--

                    if (count == 0) {
                        updateUI()
                        restoreRecyclerViewState()
                    }
                }
            )
        }

        if (tracts.isEmpty()) {
            updateUI()
        }
    }

    private fun setupButtonListener() {
        fabFragment.callbacks = this
        fabFragment.setTract(null)
    }

    /**
     * Dialogs
     */

    private fun showTractDialog(tractId: UUID) {
        showTractActionDialog(tractId, object: TractActionCallback {
            override fun onDelete(tractId: UUID) {
                tractListViewModel.deleteTract(Tract(id = tractId))
                updateTractListContent(tractListViewModel.tractsWithPicture)
                Toast.makeText(context, R.string.delete_tract_success, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showSortTractListDialog() {
        showSortListDialog(parametersViewModel.parameters, object: SortListCallbacks {
            override fun onParameterSelected(parameter: TractListParameters) {
                parametersViewModel.sortOrder = parameter.sortOrder
                parametersViewModel.sortBy = parameter.sortOption
                updateUI()
            }
        })
    }

    /**
     * Callbacks
     */

    override fun onTractSelected(tractId: UUID) {
        callbacks?.onTractSelected(tractId)
    }

    override fun onTractLongSelected(tractId: UUID) {
        showTractDialog(tractId)
    }

    override fun onTractLikeToggled(tractId: UUID) {
        tractListViewModel.toggleLike(tractId)
    }

    override fun onTractImageSelected(imageIndex: Int, tractId: UUID) {
        val list = tractListViewModel.getSavedTractWithPictures(tractId)?.pictures

        list?.toTypedArray()?.let {
            if (list.isEmpty()) {
                callbacks?.onTractSelected(tractId)
            } else {
                callbacks?.onTractPictureSelected(it, imageIndex)
            }
        }
    }

    override fun onTractSaved(tractId: UUID) {
        callbacks?.onTractSelected(tractId)
    }

    override fun onDisplayModeChanged(displayMode: TractListParameters.DisplayMode) {
        parametersViewModel.displayMode = displayMode
        updateTractListLayout()
    }

    /**
     * Companion
     */

    companion object {
        private val TAG = TractListFragment::class.simpleName
    }
}