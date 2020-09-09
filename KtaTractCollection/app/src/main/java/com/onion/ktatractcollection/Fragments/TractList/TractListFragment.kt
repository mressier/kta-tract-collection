package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Models.Tract
import java.util.*

private const val TAG = "TractListFragment"

/**
 * A fragment representing a list of Items.
 */
class TractListFragment : Fragment(), TractListCallbacks, TractDialogFragment.Callbacks {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTractSelected(tractId: UUID)
    }

    enum class Requests {
        TRACT_ACTION
    }

    enum class Dialogs {
        TRACT_ACTION
    }

    /**
     * Properties
     */

    private var callbacks: Callbacks? = null
    private val tractListViewModel: TractListViewModel by lazy {
        ViewModelProvider(this).get(TractListViewModel::class.java)
    }

    private lateinit var tractRecyclerView: RecyclerView
    private lateinit var noTractImageView: ImageView
    private lateinit var noTractButton: Button

    private lateinit var tractAdapter: TractListAdapter

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tract_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_tract -> {
                launchTractCreation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchTractCreation() {
        val tract = Tract()
        tractListViewModel.saveTract(tract)
        callbacks?.onTractSelected(tract.id)
    }

    /**
     * Update
     */
    private fun updateUI(tracts: List<Tract>) {
        tractListViewModel.saveAsTractsWithPicture(tracts)
        updateTractListUI(tractListViewModel.tractsWithPicture)
    }

    private fun updateTractListUI(tractListItems: List<TractWithPicture>) {
        val previousTracts = tractAdapter.currentList

        tractAdapter.submitList(tractListItems)
        tractAdapter.notifyDataSetChanged()

        if (previousTracts.isNotEmpty() && tractListItems.isNotEmpty()) { return }

        val noTractVisibility =
            if (tractListItems.isNotEmpty()) { View.GONE } else { View.VISIBLE }
        noTractButton.visibility = noTractVisibility
        noTractImageView.visibility = noTractVisibility
    }

    /**
     * Setup
     */

    private fun setupRecyclerView(view: View) {
        tractRecyclerView = view.findViewById(R.id.tract_list)
        tractAdapter = TractListAdapter(requireContext(), this)

        tractRecyclerView.adapter = tractAdapter
        tractRecyclerView.layoutManager = GridLayoutManager(context, 1)
    }

    private fun setupNoTractView(view: View) {
        noTractImageView = view.findViewById(R.id.no_tract_image)
        noTractButton = view.findViewById(R.id.no_tract_button)

        noTractImageView.visibility = View.GONE
        noTractButton.visibility = View.GONE
    }

    /**
     * Listener
     */

    private fun setupViewModelObserver() {
        tractListViewModel.tracts.observe(
            viewLifecycleOwner,
            { items ->
                items?.let {
                    Log.i("TAG", "Got items ${it.size}")
                    updateUI(it)
                    setupImagesForTracts(it)
                }
            }
        )
    }

    private fun setupImagesForTracts(tracts: List<Tract>) {
        tractListViewModel.saveAsTractsWithPicture(tracts)

        for (index in tracts.indices) {
            tractListViewModel.getPictures(tracts[index]).observe(
                viewLifecycleOwner,
                { items ->
                    tractListViewModel.addPicturesToTractItem(index, items)

                    Log.i(TAG, "Tract ${tracts[index].id} - Got pictures $items")

                    updateTractListUI(tractListViewModel.tractsWithPicture)
                }
            )
        }
    }

    private fun setupButtonListener() {
        noTractButton.setOnClickListener {
            launchTractCreation()
        }
    }

    /**
     * Callbacks
     */

    override fun onTractSelected(tractId: UUID) {
        callbacks?.onTractSelected(tractId)
    }

    override fun onTractLongSelected(tractId: UUID) {
        TractDialogFragment.newInstance(tractId).apply {
            setTargetFragment(this@TractListFragment, Requests.TRACT_ACTION.ordinal)
            show(this@TractListFragment.requireFragmentManager(), Dialogs.TRACT_ACTION.name)
        }
    }

    override fun onDelete(tractId: UUID) {
        tractListViewModel.deleteTract(Tract(id = tractId))

        val currentTracts = tractAdapter.currentList
        val newTracts = currentTracts.filter { it.tract.id != tractId }
        updateTractListUI(newTracts)

        Toast.makeText(context, R.string.delete_tract_success, Toast.LENGTH_LONG).show()
    }

    /**
     * Tools
     */
}