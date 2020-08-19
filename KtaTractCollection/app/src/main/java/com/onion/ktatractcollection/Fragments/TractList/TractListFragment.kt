package com.onion.ktatractcollection.Fragments.TractList

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.onion.ktatractcollection.R
import com.onion.ktatractcollection.Fragments.TractList.dummy.DummyContent
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class TractListFragment : Fragment() {

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
    private val viewModel: TractListViewModel by lazy {
        ViewModelProvider(this).get(TractListViewModel::class.java)
    }

    /**
     * View Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tract_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            view.layoutManager =  GridLayoutManager(context, 2)
            view.adapter = TractListAdapter(viewModel.tracts, callbacks)
        }
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

}