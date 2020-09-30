package com.onion.ktatractcollection.Fragments.PicturesList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.onion.ktatractcollection.Models.TractPicture
import com.onion.ktatractcollection.R
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class PicturesListFragment : Fragment(), PictureItemCallback {

    interface Callbacks {
        fun onPictureListSelected(list: Array<TractPicture>, pictureIndex: Int)
    }

    /**
     * Properties
     */
    private lateinit var tractId: UUID
    private lateinit var pictures: MutableList<TractPicture>

    /* View Model */
    private val picturesViewModel: TractPicturesViewModel by lazy {
        ViewModelProvider(this).get(TractPicturesViewModel::class.java)
    }

    /* Outlets */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PicturesListAdapter

    /* Callbacks */
    var callbacks: Callbacks? = null

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) { return }

        arguments?.let {
            it.getString("tract_id")?.let { tractId -> setTract(UUID.fromString(tractId)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pictures_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            adapter = PicturesListAdapter(this)
            recyclerView.adapter = adapter
        }
        setupViewModel()
        return view
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
     * Update
     */
    fun setTract(tractId: UUID) {
        this.tractId = tractId
        picturesViewModel.loadPicturesForTractId(tractId)
    }

    private fun updateUI() {
        adapter.submitList(picturesViewModel.getPicturesPath(pictures))
    }

    /**
     * Setup
     */

    private fun setupViewModel() {
        picturesViewModel.pictures.observe(
            viewLifecycleOwner,
            { picturesList ->
                pictures = picturesList.toMutableList()
                updateUI()
                Log.d(TAG, "Get pictures ${pictures.map { it.photoFilename }}")
            }
        )
    }

    /**
     * Callbacks
     */

    override fun onPictureSelected(path: String) {
        val index = pictures.indexOfFirst { it.photoFilename == path }
        callbacks?.onPictureListSelected(pictures.toTypedArray(), index)
    }

    override fun onDeleteButtonSelected(path: String) {
        pictures.find { it.photoFilename == path }?.let { tractPicture ->
            Log.i(TAG, "Delete picture ${tractPicture.photoFilename}")
            picturesViewModel.deletePicture(tractPicture)
            pictures.remove(tractPicture)
            updateUI()
        }
    }
    /**
     * Initialize
     */

    companion object {
        private const val TAG = "PicturesListFragment"

        private const val PARAM_TRACT_ID = "tract_id"

        fun newInstance(tractId: UUID? = null): PicturesListFragment {
            val args = Bundle().apply {
                tractId?.let { putString(PARAM_TRACT_ID, it.toString()) }
            }
            return PicturesListFragment().apply { arguments = args }
        }
    }

}