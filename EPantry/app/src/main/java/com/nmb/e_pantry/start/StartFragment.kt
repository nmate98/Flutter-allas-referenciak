package com.nmb.e_pantry.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.StartFragmentBinding
import com.nmb.e_pantry.navController
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.WarningClickListener
import com.nmb.e_pantry.recyclerviewadapters.WarningRecyclerviewAdapter
import java.util.logging.Logger


class StartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: StartFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.start_fragment, container, false
        )
        navView.setCheckedItem(0)
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = StartViewModelFactory(dataSource)
        val startViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StartViewModel::class.java)
        binding.startViewModel = startViewModel
        binding.startViewModel?.onStart()
        val adapter =
            WarningRecyclerviewAdapter(
                WarningClickListener { id ->
                    binding.startViewModel!!.onClickListener(id)
                })
        binding.warningRecyclerview.adapter = adapter
        binding.startViewModel!!.warning.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        binding.startViewModel!!.id.observe(this, Observer {
            if(navController.currentDestination!!.id != R.id.spajzNyersanyagHozzaadFragment) {
                it?.let {
                    this.findNavController()
                        .navigate(
                            StartFragmentDirections.actionStartFragmentToSpajzHozzaadFragment(
                                it
                            )
                        )
                }
            }
        })
        val manager = GridLayoutManager(this.activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.warningRecyclerview.layoutManager = manager
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        for (i in 0 until navView.menu.size()) {
            for (j in 0 until navView.menu.getItem(i).subMenu.size()) {
                navView.menu.getItem(0).subMenu.getItem(0).isChecked = false
            }
        }
    }
}