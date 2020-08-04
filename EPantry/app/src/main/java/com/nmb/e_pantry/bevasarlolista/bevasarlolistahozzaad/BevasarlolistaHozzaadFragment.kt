package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.nmb.e_pantry.databinding.BevasarlolistaHozzaadFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.BevasarlolistaRecyclerviewAdapter
import com.nmb.e_pantry.recyclerviewadapters.SpajzRecyclerviewAdapter

class BevasarlolistaHozzaadFragment : Fragment(){
    private lateinit var binding : BevasarlolistaHozzaadFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isCompleted = false
        binding  = DataBindingUtil.inflate(
            inflater, R.layout.bevasarlolista_hozzaad_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = BevasarlolistaHozzaadViewModelFactory(dataSource)
        val bevasarlolistaHozzaadViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BevasarlolistaHozzaadViewModel::class.java)
        binding.bevasarlolistaHozzaadViewModel = bevasarlolistaHozzaadViewModel

        val adat = binding.bevasarlolistaHozzaadViewModel!!.onStart()
        val atvAdapter =
            AutoCompleteTextViewAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                adat
            )
        binding.nyersanyagAtv.setAdapter(atvAdapter)
        binding.nyersanyagAtv.threshold = 1
        binding.nyersanyagAtv.setOnItemClickListener { parent, _, position, _ ->
            binding.nyersanyagAtv.setText(parent.adapter.getItem(position).toString())
            binding.bevasarlolistaHozzaadViewModel!!.onItemSelected(binding.nyersanyagAtv.text.toString())
            isCompleted = true
        }
        binding.nyersanyagAtv.doAfterTextChanged {isCompleted = false }

        val sadapter = SpajzRecyclerviewAdapter()
        binding.spajzRecyclerview.adapter = sadapter
        val smanager = GridLayoutManager(this.activity, 2)
        smanager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }

        val badapter = BevasarlolistaRecyclerviewAdapter()
        binding.bevlistaRecyclerview.adapter = badapter
        binding.bevasarlolistaHozzaadViewModel!!.bevlista.observe(this, Observer {
            it?.let {
                badapter.addHeaderAndSubmitList(it)
            }
        })
        binding.spajzRecyclerview.layoutManager = smanager
        val bmanager = GridLayoutManager(this.activity, 2)
        bmanager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.bevlistaRecyclerview.layoutManager = bmanager

        binding.bevasarlolistaHozzaadViewModel!!.mertekegyseg.observe(this, Observer {
            binding.mertekegysegTextview.text = it
        })
        binding.bevasarlolistaHozzaadViewModel!!.spajz.observe(this, Observer {
            it?.let {
                sadapter.addHeaderAndSubmitList(it)
            }
        })
        binding.hozzaadButton.setOnClickListener {
            binding.mennyisegTil.error = null
            binding.nyersanyagTil.error = null
            if ((binding.mennyisegEdittext.text.toString() != "") && (isCompleted)) {
                binding.bevasarlolistaHozzaadViewModel!!.onButtonPressed(
                    binding.nyersanyagAtv.text.toString().capitalize(),
                    binding.mennyisegEdittext.text.toString().toDouble()
                )
                binding.nyersanyagAtv.setText("")
                binding.mennyisegEdittext.setText("")
            } else {
                if ((binding.mennyisegEdittext.text.toString() == "")) {
                    binding.mennyisegTil.error = "Nem adtál meg mennyiséget!"
                }
                if (!isCompleted) {
                    binding.nyersanyagTil.error = "Nincs beállítva a mező helyesen!"
                }
            }
        }
        return binding.root
    }
}