package com.unicorpdev.ktatract.fragments.picturesList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.SelectEvent
import java.io.File
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class TractPicturesFragment : Fragment(), TractPictureViewHolder.Callback {

    interface Callbacks {
        fun onPictureSelected(pictureList: Array<File>, pictureIndex: Int)
    }

    /**
     * Properties
     */

    /* View Model */
    private val picturesViewModel: TractPicturesViewModel by lazy {
        ViewModelProvider(this).get(TractPicturesViewModel::class.java)
    }

    /* Outlets */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TractPicturesAdapter

    /* Callbacks */
    var callbacks: Callbacks? = null

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            adapter = TractPicturesAdapter(this)
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
    private fun setTract(tractId: UUID) {
        Log.d(TAG, "Set tract id: $tractId. Load pictures...")
        picturesViewModel.loadPicturesForTractId(tractId)
    }

    private fun updateUI() {
        adapter.submitList(picturesViewModel.savedPicturesFile)
    }

    /**
     * Setup
     */

    private fun setupViewModel() {
        picturesViewModel.pictures.observe(
            viewLifecycleOwner,
            { picturesList ->
                Log.d(TAG, "Get pictures ${picturesList.map { it.photoFilename }}")
                picturesViewModel.savedPictures = picturesList
                updateUI()
            }
        )
    }

    /**
     * Callbacks
     */

    override fun onPictureSelected(path: String) {
        val index = picturesViewModel.savedPictures.indexOfFirst { it.photoFilename == path }
        Log.d(TAG, "File $path - index $index")
        callbacks?.onPictureSelected(picturesViewModel.savedPicturesFile.toTypedArray(), index)
    }

    override fun onDeleteButtonSelected(path: String) {
        KtaTractAnalytics.logSelectItem(SelectEvent.DELETE_PICTURE)

        picturesViewModel.savedPictures.find { it.photoFilename == path }?.let { tractPicture ->
            Log.i(TAG, "Delete picture ${tractPicture.photoFilename}")
            picturesViewModel.deletePicture(tractPicture)
        }
    }

    /**
     * Initialize
     */

    companion object {
        private const val TAG = "PicturesListFragment"

        private const val PARAM_TRACT_ID = "tract_id"

        fun newInstance(tractId: UUID? = null): TractPicturesFragment {
            val args = Bundle().apply {
                tractId?.let { putString(PARAM_TRACT_ID, it.toString()) }
            }
            return TractPicturesFragment().apply { arguments = args }
        }
    }

}