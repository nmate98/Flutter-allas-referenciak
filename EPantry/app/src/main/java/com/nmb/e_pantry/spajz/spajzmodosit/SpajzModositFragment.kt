package com.nmb.e_pantry.spajz.spajzmodosit

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nmb.e_pantry.AutoCompleteTextViewAdapter
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.SpajzModositFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.R as r


class SpajzModositFragment : Fragment() {
    private lateinit var binding : SpajzModositFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isCompleted = false
        binding = DataBindingUtil.inflate(
            inflater, r.layout.spajz_modosit_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory =
            SpajzModositViewModelFactory(dataSource)
        val spajzModositViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SpajzModositViewModel::class.java)
        binding.spajzModositViewModel = spajzModositViewModel

        val adat = binding.spajzModositViewModel!!.onStart()

        val atvAdapter =
            AutoCompleteTextViewAdapter(
                context!!,
                R.layout.simple_list_item_1,
                adat
            )
        binding.nyersanyagAtv.setAdapter(atvAdapter)
        binding.nyersanyagAtv.threshold = 1
        binding.nyersanyagAtv.setOnItemClickListener { parent, _, position, _ ->
            binding.nyersanyagAtv.setText(parent.adapter.getItem(position).toString())
            binding.spajzModositViewModel!!.onItemSelected(binding.nyersanyagAtv.text.toString())
            isCompleted = true
        }
        binding.nyersanyagAtv.doAfterTextChanged {isCompleted = false }

        try {
            val arg = arguments?.getString("id")!!
            binding.nyersanyagAtv.setText(arg)
            binding.spajzModositViewModel!!.onItemSelected(arg)
            isCompleted = true
        } catch (e: KotlinNullPointerException) {
        }

        binding.spajzModositViewModel!!.mertekegyseg.observe(this, Observer {
            binding.mertekegysegText.text = it.toString()
        })

        binding.hozzaadButton.setOnClickListener {
            if ((binding.mennyisegEdittext.text.toString() != "")  && (isCompleted)) {
                binding.mennyisegTil.error = null
                binding.nyersanyagTil.error = null
                binding.spajzModositViewModel?.onHozzaadButtonPressed(
                    binding.nyersanyagAtv.text.toString().capitalize(),
                    binding.mennyisegEdittext.text.toString().toDouble()
                )
                binding.mertekegysegText.text = ""
                binding.mennyisegEdittext.text = null
                binding.nyersanyagAtv.setText("")
                isCompleted = false
            } else {
                if ((binding.mennyisegEdittext.text.toString() == "")) {
                    binding.mennyisegTil.error = "Nem adtál meg mennyiséget!"
                }
                if (!isCompleted) {
                    binding.nyersanyagTil.error = "Nincs beállítva a mező helyesen!"
                }
            }
        }
        binding.elveszButton.setOnClickListener {
            if ((binding.mennyisegEdittext.text.toString() != "") && (isCompleted)) {
                binding.mennyisegTil.error = null
                binding.nyersanyagTil.error = null
                binding.spajzModositViewModel?.onElveszButtonPressed(
                    binding.nyersanyagAtv.text.toString(),
                    binding.mennyisegEdittext.text.toString().toDouble()
                )
                binding.mertekegysegText.text = ""
                binding.mennyisegEdittext.text = null
                binding.nyersanyagAtv.setText("")
                isCompleted = false
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