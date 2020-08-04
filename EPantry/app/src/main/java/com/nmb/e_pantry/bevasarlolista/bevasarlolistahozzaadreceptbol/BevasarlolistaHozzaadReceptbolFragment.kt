package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaadreceptbol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.nmb.e_pantry.AutoCompleteTextViewAdapter
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.BevasarlolistaHozzaadReceptbolFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.BevasarlolistaRecyclerviewAdapter
import com.nmb.e_pantry.recyclerviewadapters.HozzavaloRecyclerviewAdapter

class BevasarlolistaHozzaadReceptbolFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BevasarlolistaHozzaadReceptbolFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.bevasarlolista_hozzaad_receptbol_fragment, container, false
        )
        var isCompleted = false
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = BevasarlolistaHozzaadReceptbolViewModelFactory(dataSource)
        val bevasarlolistaHozzaadReceptbolViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BevasarlolistaHozzaadReceptbolViewModel::class.java)
        binding.bevasarlolistaHozzaadReceptbolViewModel = bevasarlolistaHozzaadReceptbolViewModel

        val data = binding.bevasarlolistaHozzaadReceptbolViewModel!!.onStart()

        val atvAdapter =
            AutoCompleteTextViewAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                data
            )
        binding.receptAtv.setAdapter(atvAdapter)
        binding.receptAtv.threshold = 1
        binding.receptAtv.setOnItemClickListener { parent, _, position, _ ->
            binding.receptAtv.setText(parent.adapter.getItem(position).toString())
            isCompleted = true
        }
        binding.receptAtv.doAfterTextChanged {isCompleted = false }
        binding.selectButton.setOnClickListener {
            if (isCompleted) {
                binding.bevasarlolistaHozzaadReceptbolViewModel!!.onKivalasztButtonPressed(binding.receptAtv.text.toString())
            } else {
                binding.receptTil.error = "Nincs beállítva a mező helyesen!"
            }
        }

        val hadapter = HozzavaloRecyclerviewAdapter()
        binding.hianyzoRecyclerview.adapter = hadapter
        binding.hozzaadButton.setOnClickListener {
            binding.bevasarlolistaHozzaadReceptbolViewModel!!.onHozzaadButtonPressed()
        }
        binding.bevasarlolistaHozzaadReceptbolViewModel!!.getBevLista()
        val hmanager = GridLayoutManager(this.activity, 3)
        hmanager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.hianyzoRecyclerview.layoutManager = hmanager

        val badapter = BevasarlolistaRecyclerviewAdapter()
        binding.bevlistaRecyclerview.adapter = badapter
        val bmanager = GridLayoutManager(this.activity, 3)
        bmanager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.bevlistaRecyclerview.layoutManager = bmanager

        binding.bevasarlolistaHozzaadReceptbolViewModel!!.bevlista.observe(this, Observer {
            it?.let {
                badapter.addHeaderAndSubmitList(it)
            }
        })
        binding.bevasarlolistaHozzaadReceptbolViewModel!!.hianyzo.observe(this, Observer {
            it?.let {
                hadapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }
}