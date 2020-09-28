package com.onion.ktatractcollection.Fragments.PicturesList

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
import com.onion.ktatractcollection.shared.fragments.ImageDialogFragment
import java.util.*

interface PictureListCallbacks {
    fun onPictureSelected(path: String)
    fun onDeleteButtonSelected(path: String)
}

/**
 * A fragment representing a list of Items.
 */
class PicturesListFragment : Fragment(), PictureListCallbacks {

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
            adapter = PicturesListAdapter(requireContext(), this)
            recyclerView.adapter = adapter
        }
        setupViewModel()
        return view
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
        val intent = ImageDialogFragment.newInstancePath(path)
        intent.show(requireActivity().supportFragmentManager, DIALOG_SHOW_PICTURE)
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
        private const val DIALOG_SHOW_PICTURE = "show_picture"

        private const val PARAM_TRACT_ID = "tract_id"

        fun newInstance(tractId: UUID? = null): PicturesListFragment {
            val args = Bundle().apply {
                tractId?.let { putString(PARAM_TRACT_ID, it.toString()) }
            }
            return PicturesListFragment().apply { arguments = args }
        }
    }

}