package com.unicorpdev.ktatract.fragments.collectionList

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.collectionList.list.TractCollectionCallback
import com.unicorpdev.ktatract.fragments.collectionList.list.CollectionListFragment
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics
import com.unicorpdev.ktatract.shared.analytics.KtaTractAnalytics.*
import com.unicorpdev.ktatract.shared.extensions.dialogs.DialogAction
import com.unicorpdev.ktatract.shared.extensions.dialogs.showActionDialog
import com.unicorpdev.ktatract.shared.fragments.AddButtonFragment
import com.unicorpdev.ktatract.shared.fragments.listHeader.ListHeaderFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AllCollectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllCollectionsFragment : Fragment(),
    CollectionListFragment.Callbacks,
    AddButtonFragment.Callbacks
{

    interface Callbacks {
        fun onSelectCollection(collectionId: UUID)
        fun onCreateCollection(collectionId: UUID)
        fun onUpdateCollection(collectionId: UUID)
    }

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private lateinit var headerFragment: ListHeaderFragment

    private var callbacks: Callbacks? = null

    private val viewModel by viewModels<AllCollectionsViewModel>()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChildFragments()
        setupHeaderFragment()
    }

    override fun onAttach(context: Context) {
        callbacks = context as? Callbacks ?: parentFragment as? Callbacks
        super.onAttach(context)
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /***********************************************************************************************
     * Tract Collection Callback
     **********************************************************************************************/

    override fun onSelectCollection(collectionId: UUID) {
        callbacks?.onSelectCollection(collectionId)
    }

    override fun onSelectMoreActions(collectionId: UUID) {
        showMoreActions(collectionId)
    }

    override fun onItemCountChanged(itemCount: Int) {
        headerFragment.updateItemCount(itemCount)
    }

    override fun onAddButtonSelected() {
        KtaTractAnalytics.logSelectItem(SelectEvent.CREATE_COLLECTION)
        val collectionId = viewModel.createCollection()
        callbacks?.onCreateCollection(collectionId)
    }

    /***********************************************************************************************
     * Dialogs
     **********************************************************************************************/

    private fun showMoreActions(collectionId: UUID) {
        GlobalScope.async {
            val isEditable = viewModel.isEditable(collectionId)
            val isDeletable = viewModel.isDeletable(collectionId)

            val actions = mutableListOf<DialogAction>()

            if (isEditable) {
                actions.add(
                    DialogAction(
                        R.string.modify_collection,
                        callback = { updateCollection(collectionId) }
                    )
                )
            }
            if (isDeletable) {
                actions.add(
                    DialogAction(
                        R.string.delete_collection,
                        android.R.color.holo_red_light,
                        callback = { deleteCollection(collectionId) }
                    )
                )
            }

            requireActivity().runOnUiThread {
                showActionDialog(actions = actions.toTypedArray())
            }
        }
    }

    private fun updateCollection(collectionId: UUID) {
        KtaTractAnalytics.logSelectItem(SelectEvent.MODIFY_COLLECTION)
        callbacks?.onUpdateCollection(collectionId)
    }

    private fun deleteCollection(collectionId: UUID) {
        KtaTractAnalytics.logSelectItem(SelectEvent.DELETE_COLLECTION)
        viewModel.deleteCollection(collectionId)
    }

    /***********************************************************************************************
     * Setup
     **********************************************************************************************/

    private fun setupChildFragments() {
        headerFragment =
            childFragmentManager.findFragmentById(R.id.headerFragment) as ListHeaderFragment
    }

    private fun setupHeaderFragment() {
        headerFragment.setCounterFormatString(R.string.collection_count)
        headerFragment.shouldShowDisplayModeButton(false)
    }

    /***********************************************************************************************
     * Companion
     **********************************************************************************************/

    companion object {
        @JvmStatic
        fun newInstance() = AllCollectionsFragment()
    }

}