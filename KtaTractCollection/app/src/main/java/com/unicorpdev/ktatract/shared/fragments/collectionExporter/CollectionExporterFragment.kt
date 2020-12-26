package com.unicorpdev.ktatract.shared.fragments.collectionExporter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unicorpdev.ktatract.R
import com.unicorpdev.ktatract.fragments.tractList.AllTractsFragment
import com.unicorpdev.ktatract.models.MimeType
import com.unicorpdev.ktatract.shared.extensions.dialogs.showErrorDialog
import com.unicorpdev.ktatract.shared.extensions.dialogs.showLoadingDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.FileNotFoundException
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionExporterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionExporterFragment : Fragment() {

    /***********************************************************************************************
     * Properties
     **********************************************************************************************/

    private val viewModel by viewModels<CollectionExporterViewModel>()

    /***********************************************************************************************
     * View Life Cycle
     **********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_exporter, container, false)
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
     * Methods
     **********************************************************************************************/

    fun importCollection() {
        selectZipFile()
    }

    fun exportCollection(collectionId: UUID?) {
        viewModel.collectionId = collectionId
        selectZipDirectory()
    }

    /***********************************************************************************************
     * Import / Export
     **********************************************************************************************/

    private fun exportCollection(uri: Uri) {
        val dialog = showLoadingDialog()
        GlobalScope.async {
            viewModel.exportCollection(requireContext(), uri)
            requireActivity().runOnUiThread { dialog.dismiss() }
        }
    }

    private fun importCollection(uri: Uri) {
        val dialog = showLoadingDialog()
        viewModel.importCollection(
            requireActivity(),
            uri,
            object : CollectionExporterViewModel.ImportCallback {
                override fun onSuccess() {
                    dialog.dismiss()
                }

                override fun onFailed(error: Error) {
                    dialog.dismiss()

                    val title = getString(R.string.import_failed)
                    val text = getString(R.string.import_missing_files).format(error.message)
                    showErrorDialog(title, text)
                }
            }
        )
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
     * Companion
     **********************************************************************************************/

    companion object {

        private const val GET_ZIP_DIRECTORY_REQUEST = 100
        private const val GET_ZIP_FILE_REQUEST = 101

        @JvmStatic
        fun newInstance() = CollectionExporterFragment()
    }
}