package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.onion.ktatractcollection.Fragments.Fab.FabImageMenuFragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractDialogFragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListDialogFragment
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParameters
import com.onion.ktatractcollection.Fragments.TractList.dialogs.TractListParametersViewModel
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Models.Tract
import java.util.*

private const val TAG = "TractListFragment"

private const val REQUEST_TRACT_ACTION = 0
private const val REQUEST_TRACT_LIST_ACTION = 1

private const val DIALOG_TRACT_ACTION = "dialog_tract_action"
private const val DIALOG_TRACT_LIST_ACTION = "dialog_tract_list_action"


/**
 * A fragment representing a list of Items.
 */
class TractListFragment : Fragment(),
    TractListCallbacks,
    TractDialogFragment.Callbacks,
    TractListDialogFragment.Callbacks,
    FabImageMenuFragment.Callbacks
{

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTractSelected(tractId: UUID)
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

    private lateinit var tractRecyclerView: RecyclerView
    private lateinit var noTractImageView: ImageView
    private lateinit var noTractText: TextView
    private lateinit var fabFragment: FabImageMenuFragment

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
        val view = inflater.inflate(R.layout.fragment_tract_list, container, false)
        setupRecyclerView(view)
        setupNoTractView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObserver()
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

    /**
     * Menu
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_list_menu, menu)
        updateMenuUI(parametersViewModel.reversedDisplayMode,
            menu.findItem(R.id.grid_or_list_tract))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sort_list -> {
                showTractListDialog()
                true
            }
            R.id.grid_or_list_tract -> {
                toggleListMenuItem(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleListMenuItem(item: MenuItem) {
        parametersViewModel.reverseDisplayMode()

        updateMenuUI(parametersViewModel.reversedDisplayMode, item)
        updateTractListLayout(parametersViewModel.displayMode)
    }

    /**
     * Update
     */
    private fun updateUI() {
        val tracts = parametersViewModel.getDisplayedTracts(currentTracts)
        tractListViewModel.saveAsTractsWithPicture(tracts)

        updateTractListLayout(parametersViewModel.displayMode)
        updateTractListContent(tractListViewModel.tractsWithPicture)
    }

    private fun updateTractListContent(
        tractListItems: List<TractWithPicture>
    ) {
        val previousTracts = tractAdapter.currentList

        tractAdapter.parameters = parametersViewModel.parameters
        tractAdapter.submitList(tractListItems)
        tractAdapter.notifyDataSetChanged()

        if (previousTracts.isNotEmpty() && tractListItems.isNotEmpty()) { return }

        val noTractVisibility =
            if (tractListItems.isNotEmpty()) { View.GONE } else { View.VISIBLE }
        noTractImageView.visibility = noTractVisibility
        noTractText.visibility = noTractVisibility
    }

    private fun updateTractListLayout(displayMode: TractListParameters.DisplayMode) {
        tractLayout.spanCount = displayMode.spanCount
        tractRecyclerView.layoutManager = tractLayout
        tractRecyclerView.adapter = tractAdapter
    }

    private fun updateMenuUI(displayMode: TractListParameters.DisplayMode, item: MenuItem) {
        item.setIcon(displayMode.iconId)
        item.setTitle(displayMode.titleId)
    }

    /**
     * Setup
     */

    private fun setupRecyclerView(view: View) {
        tractRecyclerView = view.findViewById(R.id.tract_list)
        tractAdapter = TractListAdapter(requireContext(), this)
        tractLayout = GridLayoutManager(context, 1)

        tractRecyclerView.adapter = tractAdapter
        tractRecyclerView.layoutManager = tractLayout
    }

    private fun setupNoTractView(view: View) {
        noTractImageView = view.findViewById(R.id.no_tract_image)
        noTractText = view.findViewById(R.id.no_tract_text)
        fabFragment = childFragmentManager.findFragmentById(R.id.fab_fragment) as FabImageMenuFragment

        noTractImageView.visibility = View.GONE
        noTractText.visibility = View.GONE
    }

    /**
     * Listener
     */

    private fun setupViewModelObserver() {
        tractListViewModel.tracts.observe(
            owner = viewLifecycleOwner,
            onChanged = { items ->
                items.let {
                    Log.i("TAG", "Got items ${it.size}")
                    currentTracts = items
                    updateUI()
                    setupImagesForTracts()
                }
            }
        )
    }

    private fun setupImagesForTracts() {
        val tracts = parametersViewModel.getDisplayedTracts(currentTracts)
        tractListViewModel.saveAsTractsWithPicture(tracts)

        for (tractId in tracts.map { it.id }) {
            tractListViewModel.getPictures(tractId).observe(
                owner = viewLifecycleOwner,
                onChanged = { items ->
                    tractListViewModel.addPicturesToTractItem(tractId, items)

                    Log.i(TAG, "Tract $tractId - Got pictures $items")

                    updateTractListContent(tractListViewModel.tractsWithPicture)
                }
            )
        }
    }

    private fun setupButtonListener() {
        fabFragment.callbacks = this
    }

    /**
     * Dialogs
     */

    private fun showTractDialog(tractId: UUID) {
        TractDialogFragment.newInstance(tractId).apply {
            setTargetFragment(this@TractListFragment, REQUEST_TRACT_ACTION)
            show(this@TractListFragment.requireFragmentManager(), DIALOG_TRACT_ACTION)
        }
    }

    private fun showTractListDialog() {
        TractListDialogFragment.newInstance(parametersViewModel.parameters).apply {
            setTargetFragment(this@TractListFragment, REQUEST_TRACT_LIST_ACTION)
            show(this@TractListFragment.requireFragmentManager(), DIALOG_TRACT_LIST_ACTION)
        }
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

    override fun onDelete(tractId: UUID) {
        tractListViewModel.deleteTract(Tract(id = tractId))

        updateTractListContent(tractListViewModel.tractsWithPicture)

        Toast.makeText(context, R.string.delete_tract_success, Toast.LENGTH_LONG).show()
    }

    override fun onParameterSelected(parameter: TractListParameters) {
        parametersViewModel.parameters = parameter
        updateUI()
    }

    override fun onTractSaved(tractId: UUID) {
        callbacks?.onTractSelected(tractId)
    }

}