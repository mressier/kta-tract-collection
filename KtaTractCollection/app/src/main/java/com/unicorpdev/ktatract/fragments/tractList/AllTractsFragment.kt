package com.unicorpdev.ktatract.fragments.tractList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.fragments.fab.FabImageMenuFragment
import com.unicorpdev.ktatract.fragments.tractList.parameters.TractListParameters
import com.unicorpdev.ktatract.shared.fragments.listHeader.ListHeaderFragment
import com.unicorpdev.ktatract.fragments.tractList.parameters.DisplayMode
import com.unicorpdev.ktatract.fragments.tractList.list.TractListCallbacks
import com.unicorpdev.ktatract.fragments.tractList.list.TractListFragment
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.Tract
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import com.unicorpdev.ktatract.shared.extensions.dialogs.*
import com.unicorpdev.ktatract.shared.extensions.hideKeyboard
import com.unicorpdev.ktatract.shared.extensions.setIsVisible
import com.unicorpdev.ktatract.shared.fragments.collectionExporter.CollectionExporterFragment
import kotlinx.android.synthetic.main.fragment_all_tracts.*
import java.io.File
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class AllTractsFragment :
    Fragment(),
    TractListCallbacks,
    FabImageMenuFragment.Callbacks,
    ListHeaderFragment.Callbacks
{
    /**  Required interface for hosting activities **/

    interface Callbacks {
        fun onEditTract(tractId: UUID)
        fun onTractSelected(tract: TractWithPicture)
        fun onTractPictureSelected(tract: TractWithPicture, pictureIndex: Int)
        fun onAboutPageSelected()
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private var callbacks: Callbacks? = null
    var onSearch: ((String?) -> Unit)? = null

    /** View Models **/

    private val viewModel by viewModels<AllTractsViewModel>()
    
    /** Parameters **/

    var parameters: TractListParameters
        get() = tractsFragment.parameters
        set(value) { tractsFragment.parameters = value }

    /** Outlets **/

    private lateinit var fabFragment: FabImageMenuFragment
    private lateinit var tractsFragment: TractListFragment
    private lateinit var headerFragment: ListHeaderFragment
    private lateinit var exporterFragment: CollectionExporterFragment

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_tracts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
        setupButtonListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    /***********************************************************************************************
     * Menu
     **********************************************************************************************/
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_list_menu, menu)

        setupSearchItemMenu(menu)
        tractsFragment.searchText = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sort_list -> {
                KtaTractAnalytics.logSelectItem(SelectEvent.SORT_LIST)
                showSortTractListDialog()
                true
            }
            R.id.export_collection -> {
                KtaTractAnalytics.logSelectItem(SelectEvent.EXPORT_COLLECTION)
                exporterFragment.exportCollection(viewModel.collectionId)
                true
            }
            R.id.import_collection -> {
                KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_COLLECTION)
                exporterFragment.importCollection()
                true
            }
            R.id.about -> {
                KtaTractAnalytics.logSelectItem(SelectEvent.ABOUT)
                callbacks?.onAboutPageSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /***********************************************************************************************
     * Methods
     **********************************************************************************************/

    fun loadCollection(collectionId: UUID?) {
        viewModel.collectionId = collectionId
        if (view != null) {
            tractsFragment.loadCollection(collectionId)
        }
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupView() {
        setupNoTractView()
        setupFragments()
        setupHeaderFragment()
        setupTractsFragment()
    }

    private fun setupFragments() {
        fabFragment =
            childFragmentManager.findFragmentById(R.id.fabFragment) as FabImageMenuFragment
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as ListHeaderFragment
        tractsFragment =
            childFragmentManager.findFragmentById(R.id.tractRecyclerFragment) as TractListFragment
        exporterFragment =
            childFragmentManager
                .findFragmentById(R.id.exportCollectionFragment) as CollectionExporterFragment
    }

    private fun setupHeaderFragment() {
        headerFragment.setCounterFormatString(R.string.tract_count)
    }

    private fun setupNoTractView() {
        setNoTractViewIsVisible(isVisible = false, animated = false)
        noTractResultText.setIsVisible(isVisible = false, animated = false)
        tractListLayout.setIsVisible(isVisible = true, animated = false)
    }

    private fun setNoTractViewIsVisible(isVisible: Boolean, animated: Boolean = true) {
        noTractImageView.setIsVisible(isVisible, animated)
        noTractText.setIsVisible(isVisible, animated)
    }

    private fun setupTractsFragment() {
        Log.d(TAG, "Tract fragment with collection id ${viewModel.collectionId}")
        viewModel.collectionId?.let {
            tractsFragment.loadCollection(it)
            fabFragment.setCollection(it)
        }
    }

    /***********************************************************************************************
     * Listeners
     **********************************************************************************************/
    
    private fun setupButtonListener() {
        fabFragment.callbacks = this
        fabFragment.setTract(null)
        fabFragment.setShouldShowMultipleImportButton(true)
    }

    private fun setupSearchItemMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    tractsFragment.searchText = query ?: ""
                    onSearch?.let { it(query) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    tractsFragment.searchText = newText ?: ""
                    onSearch?.let { it(newText) }
                    return true
                }
            })

        searchView?.setOnSearchClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.SEARCH)
        }
    }

    private fun setupObservers() {
    }

    /***********************************************************************************************
     * Dialogs
     **********************************************************************************************/

    private fun showTractDialog(tractId: UUID) {
        val actions = arrayOf(
            DialogAction(
                R.string.modify_tract,
                callback = { updateTract(tractId) }
            ),
            DialogAction(
                R.string.delete_tract,
                android.R.color.holo_red_light,
                callback = { deleteTract(tractId) }
            )
        )

        showActionDialog(actions = actions)
    }

    private fun updateTract(tractId: UUID) {
        KtaTractAnalytics.logSelectItem(SelectEvent.MODIFY_TRACT)
        callbacks?.onEditTract(tractId)
    }

    private fun deleteTract(tractId: UUID) {
        KtaTractAnalytics.logSelectItem(SelectEvent.DELETE_TRACT)
        viewModel.deleteTract(tractId)
        Toast.makeText(context, R.string.delete_tract_success, Toast.LENGTH_SHORT)
            .show()
    }

    private fun showSortTractListDialog() {
        showSortListDialog(parameters, object: SortListCallbacks {
            override fun onParameterSelected(parameter: TractListParameters) {
                parameters = parameter
            }
        })
    }

    /***********************************************************************************************
     * Callbacks
     **********************************************************************************************/

    override fun onTractSelected(tract: TractWithPicture) {
        requireActivity().hideKeyboard()
        callbacks?.onTractSelected(tract)
    }

    override fun onTractMoreActionsSelected(tractId: UUID) {
        showTractDialog(tractId)
    }

    override fun onTractToggleFavorite(tractId: UUID, isFavorite: Boolean) {
        viewModel.updateTractIsFavorite(tractId, isFavorite)
    }

    override fun onItemCountChanged(numberOfItems: Int) {
        headerFragment.updateItemCount(numberOfItems)

        val hasContent = numberOfItems > 0

        if (tractsFragment.isInSearchMode) {
            noTractResultText.setIsVisible(!hasContent)
            setNoTractViewIsVisible(false)
        } else {
            noTractResultText.setIsVisible(false)
            setNoTractViewIsVisible(!hasContent)
        }

        tractListLayout.setIsVisible(hasContent)
    }

    override fun onTractSaved(tractId: UUID) {
        callbacks?.onEditTract(tractId)
    }

    override fun onTractsSaved(tractIds: Array<UUID>) {
        Toast.makeText(
            requireContext(),
            getString(R.string.add_tracts_success).format(tractIds.size),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisplayModeChanged(displayMode: DisplayMode) {
        tractsFragment.displayMode = displayMode
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/
    
    companion object {
        private val TAG = AllTractsFragment::class.simpleName

        @JvmStatic
        fun newInstance() = AllTractsFragment()
    }
}