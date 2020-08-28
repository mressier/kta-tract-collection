package com.onion.ktatractcollection.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.onion.ktatractcollection.Fragments.TractList.TractListFragment
import com.onion.ktatractcollection.Fragments.TractList.TractListFragmentDirections
import com.onion.ktatractcollection.R
import java.util.*

class MainActivity : AppCompatActivity(), TractListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onTractSelected(tractId: UUID) {
        val action = TractListFragmentDirections.showTract(tractId.toString())
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

}