package com.nmb.e_pantry.recept.recepthozzaad

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
import com.nmb.e_pantry.database.Hozzavalok
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.ReceptHozzaadFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.HozzavaloRecyclerviewAdapter

class ReceptHozzaadFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ReceptHozzaadFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.recept_hozzaad_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = ReceptHozzaadViewModelFactory(dataSource)
        val receptHozzaadViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReceptHozzaadViewModel::class.java)
        binding.receptHozzaadViewModel = receptHozzaadViewModel
        var isCompleted = false
        val adat: List<String> = binding.receptHozzaadViewModel!!.onStart()
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
            binding.receptHozzaadViewModel!!.onItemSelected(
                binding.nyersanyagAtv.text.toString()
            )
            isCompleted = true
            binding.nyersanyagTil.error = null
        }

        binding.receptHozzaadButton.setOnClickListener {
            if (binding.rNevEdittext.text.toString() != "") {
                binding.rNevTil.error = null
                binding.receptHozzaadViewModel!!.addRecept(
                    binding.rNevEdittext.text.toString().capitalize()
                )
                binding.rNevEdittext.setText("")
            } else {
                binding.rNevTil.error = "Nem adtál meg nevet a receptnek!"
            }

        }
        val radapter = HozzavaloRecyclerviewAdapter()
        binding.hozzavaloRecyclerview.adapter = radapter
        val manager = GridLayoutManager(this.activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.hozzavaloRecyclerview.layoutManager = manager

        binding.receptHozzaadViewModel!!.mertekegyseg.observe(this, Observer {
            binding.mertekegysegTextview.text = it.toString()
        })
        binding.receptHozzaadViewModel!!.recept.observe(this, Observer {
            it?.let {
                radapter.addHeaderAndSubmitList(it)
            }
        })
        binding.nyersanyagAtv.doAfterTextChanged { isCompleted = false }
        binding.HozzaadButton.setOnClickListener {
            binding.mennyisegTil.error = null
            binding.nyersanyagTil.error = null
            if ((isCompleted) && (binding.mennyisegEdittext.text.toString() != "")) {
                binding.receptHozzaadViewModel!!.onHozzaadButtonClicked(
                    binding.nyersanyagAtv.text.toString(),
                    binding.mennyisegEdittext.text.toString().toDouble(),
                    binding.mertekegysegTextview.text.toString()
                )
                binding.nyersanyagAtv.setText("")
                binding.mennyisegEdittext.setText("")
                binding.mertekegysegTextview.text = ""
            } else {
                if ((binding.mennyisegEdittext.text.toString() == "")) {
                    binding.mennyisegTil.error = "Nem adtál meg mennyiséget!"
                }
                if (!isCompleted) {
                    binding.nyersanyagTil.error = "Nincs beállítva helyesen a mező!"
                }
            }
        }
        binding.torolButton.setOnClickListener {
            binding.receptHozzaadViewModel!!.onTorolButtonClicked()
        }

        return binding.root
    }


}
