package com.nmb.e_pantry.bevasarlolista.bevasarlolistamegtekint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.BevasarlolistaMegtekintFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.DeletableRecyclerviewAdapter
import com.nmb.e_pantry.recyclerviewadapters.DeleteClickListener


class BevasarlolistaMegtekintFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BevasarlolistaMegtekintFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.bevasarlolista_megtekint_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = BevasarlolistaMegtekintViewModelFactory(dataSource)
        val bevasarlolistaHozzaadViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BevasarlolistaMegtekintViewModel::class.java)
        binding.bevasarlolistaMegtekintViewModel = bevasarlolistaHozzaadViewModel

        binding.bevasarlolistaMegtekintViewModel?.onStart()

        binding.bevasarlolistaMegtekintViewModel!!.getOsszKoltseg()
        binding.bevasarlolistaMegtekintViewModel!!.koltsegString.observe(this, Observer {
            binding.osszegTextview.text = it
        })

        val adapter =
            DeletableRecyclerviewAdapter(
                DeleteClickListener { nev ->
                    binding.bevasarlolistaMegtekintViewModel!!.deleteItem(nev)
                })
        binding.bevlistaRecyclerview.adapter = adapter

        val manager = GridLayoutManager(this.activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.bevlistaRecyclerview.layoutManager = manager

        binding.bevasarlolistaMegtekintViewModel!!.bevlista.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        navView.menu.getItem(2).subMenu.getItem(0).isChecked = true
    }

    override fun onPause() {
        super.onPause()
        navView.menu.getItem(2).subMenu.getItem(0).isChecked = false
    }
}