package com.onion.ktatractcollection.Fragments.TractPictures

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onion.ktatractcollection.R
import java.io.File
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class PicturesFragment : Fragment() {

    private lateinit var tractID: UUID

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            tractID = UUID.fromString(it.getString("tract_id"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pictures_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = TractPicturesAdapter(requireContext())
            }
        }
        return view
    }

    /**
     * Initialize
     */

    companion object {

        @JvmStatic
        fun newInstance(tractId: UUID) =
            PicturesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("tract_id", tractId.toString())
                }
            }
    }
}