package com.nmb.e_pantry.recept.receptmegtekint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.nmb.e_pantry.AutoCompleteTextViewAdapter
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.ReceptMegtekintFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.HozzavaloRecyclerviewAdapter

class ReceptMegtekintFragment : Fragment() {
    private lateinit var binding: ReceptMegtekintFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isCompleted = false
        binding =
            DataBindingUtil.inflate(inflater, R.layout.recept_megtekint_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = ReceptMegtekintViewModelFactory(dataSource)
        val spajzMegtekintViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ReceptMegtekintViewModel::class.java)
        binding.receptMegtekintViewModel = spajzMegtekintViewModel

        val adat: MutableList<String> = binding.receptMegtekintViewModel!!.onStart()
        val atvAdapter =
            AutoCompleteTextViewAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                adat
            )
        binding.receptAtv.setOnItemClickListener{ _, _, _, _ ->
            isCompleted = true
        }
        binding.receptAtv.setAdapter(atvAdapter)
        binding.receptAtv.threshold = 1
        binding.receptAtv.doAfterTextChanged {isCompleted = false }
        binding.megtekintButton.setOnClickListener {
            if (isCompleted) {
                binding.receptMegtekintViewModel!!.onButtonClicked(binding.receptAtv.text.toString())
            } else {
                Toast.makeText(context,"Válassz receptet a legördül menüből!", Toast.LENGTH_SHORT).show()
            }
        }

        val adapter = HozzavaloRecyclerviewAdapter()
        binding.hozzavaloRecyclerview.adapter = adapter
        val manager = GridLayoutManager(this.activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.hozzavaloRecyclerview.layoutManager = manager

        binding.receptMegtekintViewModel!!.recept.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }
}