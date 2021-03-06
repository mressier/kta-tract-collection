package com.unicorpdev.ktatract.Fragments.TractList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.unicorpdev.ktatract.Fragments.Fab.FabImageMenuFragment
import com.unicorpdev.ktatract.Fragments.TractList.parameters.TractListParameters
import com.unicorpdev.ktatract.Fragments.TractList.header.TractListHeaderFragment
import com.unicorpdev.ktatract.Fragments.TractList.parameters.DisplayMode
import com.unicorpdev.ktatract.Fragments.TractList.list.TractListCallbacks
import com.unicorpdev.ktatract.Fragments.TractList.list.TractListFragment
import com.unicorpdev.ktatract.models.MimeType
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.models.TractPicture
import com.unicorpdev.ktatract.models.TractWithPicture
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import com.unicorpdev.ktatract.shared.dialogs.*
import com.unicorpdev.ktatract.shared.extensions.hideKeyboard
import com.unicorpdev.ktatract.shared.extensions.setIsVisible
import kotlinx.android.synthetic.main.fragment_tract_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.io.FileNotFoundException
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class AllTractsFragment :
    Fragment(),
    TractListCallbacks,
    FabImageMenuFragment.Callbacks,
    TractListHeaderFragment.Callbacks
{
    /**  Required interface for hosting activities **/

    interface Callbacks {
        fun onTractSelected(tractId: UUID)
        fun onTractPictureSelected(list: Array<File>, pictureIndex: Int)
        fun onAboutPageSelected()
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private var callbacks: Callbacks? = null

    /** View Models **/

    private val allTractsViewModel: AllTractsViewModel by lazy {
        ViewModelProvider(this).get(AllTractsViewModel::class.java)
    }

    /** Parameters **/

    var parameters: TractListParameters
        get() = tractsFragment.parameters
        set(value) { tractsFragment.parameters = value }

    /** Outlets **/

    private lateinit var fabFragment: FabImageMenuFragment
    private lateinit var tractsFragment: TractListFragment
    private lateinit var headerFragment: TractListHeaderFragment

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
        return inflater.inflate(R.layout.fragment_tract_list, container, false)
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
     * Intents Results
     **********************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_ZIP_DIRECTORY_REQUEST ->
                    data?.data?.let { uri -> exportCollection(uri) }
                GET_ZIP_FILE_REQUEST ->
                    data?.data?.let { uri -> importCollection(uri) }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                selectZipDirectory()
                true
            }
            R.id.import_collection -> {
                KtaTractAnalytics.logSelectItem(SelectEvent.IMPORT_COLLECTION)
                selectZipFile()
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
     * Intents
     **********************************************************************************************/

    private fun selectZipDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, GET_ZIP_DIRECTORY_REQUEST)
    }

    private fun selectZipFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = MimeType.ZIP.string
        startActivityForResult(intent, GET_ZIP_FILE_REQUEST)
    }

    /***********************************************************************************************
     * Import / Export
     **********************************************************************************************/

    private fun exportCollection(uri: Uri) {
        val dialog = showLoadingDialog()
        GlobalScope.async {
            allTractsViewModel.exportCollection(requireContext(), uri)
            requireActivity().runOnUiThread { dialog.dismiss() }
        }
    }

    private fun importCollection(uri: Uri) {
        val dialog = showLoadingDialog()
        GlobalScope.async {
            try {
                allTractsViewModel.importCollection(requireContext(), uri)
                requireActivity().runOnUiThread { dialog.dismiss() }
            } catch (e: FileNotFoundException) {
                requireActivity().runOnUiThread {
                    dialog.dismiss()

                    val title = getString(R.string.import_failed)
                    val text = getString(R.string.import_missing_files).format(e.message)
                    showErrorDialog(title, text)
                }
            }
        }
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupView() {
        setupNoTractView()
        setupFragments()
    }

    private fun setupFragments() {
        fabFragment =
            childFragmentManager.findFragmentById(R.id.fabFragment) as FabImageMenuFragment
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as TractListHeaderFragment
        tractsFragment =
            childFragmentManager.findFragmentById(R.id.tractRecyclerFragment) as TractListFragment
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
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    tractsFragment.searchText = newText ?: ""
                    return true
                }
            })

        searchView?.setOnSearchClickListener {
            KtaTractAnalytics.logSelectItem(SelectEvent.SEARCH)
        }
    }

    private fun setupObservers() {
        allTractsViewModel.tracts.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                allTractsViewModel.savedTracts = items
            })

        allTractsViewModel.pictures.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                allTractsViewModel.savedPictures = items
            })
    }

    /***********************************************************************************************
     * Dialogs
     **********************************************************************************************/

    private fun showTractDialog(tractId: UUID) {
        showTractActionDialog(tractId, object: TractActionCallback {
            override fun onDelete(tractId: UUID) {
                KtaTractAnalytics.logSelectItem(SelectEvent.DELETE_TRACT)
                allTractsViewModel.deleteTract(tractId)
                Toast.makeText(context, R.string.delete_tract_success, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onModify(tractId: UUID) {
                KtaTractAnalytics.logSelectItem(SelectEvent.MODIFY_TRACT)
                callbacks?.onTractSelected(tractId)
            }
        })
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

    override fun onTractSelected(tractId: UUID) {
        requireActivity().hideKeyboard()
        callbacks?.onTractSelected(tractId)
    }

    override fun onTractLongSelected(tractId: UUID) {
        showTractDialog(tractId)
    }

    override fun onTractToggleFavorite(tractId: UUID, isFavorite: Boolean) {
        allTractsViewModel.updateTractIsFavorite(tractId, isFavorite)
    }

    override fun onTractImageSelected(imageIndex: Int, tract: TractWithPicture) {
        requireActivity().hideKeyboard()
        if (tract.pictures.isEmpty()) {
            callbacks?.onTractSelected(tract.tract.id)
        } else {
            callbacks?.onTractPictureSelected(
                tract.pictures.map { tract.picturesFile[it.id] ?: error("") }.toTypedArray(),
                imageIndex
            )
        }
    }

    override fun onItemCountChanged(numberOfItems: Int) {
        headerFragment.updateTractNumber(numberOfItems)

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
        callbacks?.onTractSelected(tractId)
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

        private const val GET_ZIP_DIRECTORY_REQUEST = 0
        private const val GET_ZIP_FILE_REQUEST = 1
    }
}