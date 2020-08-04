package com.nmb.e_pantry.spajz.spajznyersanyagmodosit

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
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.SpajzNyersanyagModositFragmentBinding

class SpajzNyersanyagModositFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SpajzNyersanyagModositFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.spajz_nyersanyag_modosit_fragment,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = SpajzNyersanyagModositViewModelFactory(dataSource)
        val spajzNyersanyagModositViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SpajzNyersanyagModositViewModel::class.java)
        var isCompleted = false
        binding.spajzNyersanyagModositViewModel = spajzNyersanyagModositViewModel

        val adat = binding.spajzNyersanyagModositViewModel!!.onStart()
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
            binding.spajzNyersanyagModositViewModel!!.onItemSelected(
                binding.nyersanyagAtv.text.toString()
            )
            isCompleted = true
            binding.nyersanyagTil.error = null
        }
        binding.kihagycheckBox.setOnClickListener {
            binding.spajzNyersanyagModositViewModel!!.onKihagyCBClicked(binding.kihagycheckBox.isChecked)
        }
        binding.nyersanyagAtv.doAfterTextChanged { isCompleted = false }
        binding.spajzNyersanyagModositViewModel!!.kihagy.observe(this, Observer {
            binding.fMennyEdittext.isEnabled = !it
        })
        binding.spajzNyersanyagModositViewModel!!.nyersanyag.observe(this, Observer {
            binding.arTil.hint = "Ár = ${it.Ar}"
            binding.fMennyTil.hint = "Figyelmeztetési mennyiség = ${it.F_Menny}"
            binding.kihagycheckBox.isChecked = it.F_Menny == -1.0
            binding.spajzNyersanyagModositViewModel!!.onKihagyCBClicked(it.F_Menny == -1.0)
            binding.spajzNyersanyagModositViewModel!!.getMertekegyseg(it.M_ID)

        })
        binding.spajzNyersanyagModositViewModel!!.mertekegyseg.observe(this, Observer {
            binding.mertekegysegTextview.text = it
        })

        binding.modositButton.setOnClickListener {
            binding.arTil.error = null
            binding.fMennyTil.error = null
            binding.nyersanyagTil.error = null
            if ((isCompleted) && ((binding.spajzNyersanyagModositViewModel!!.kihagy.value!!) || (((!binding.spajzNyersanyagModositViewModel!!.kihagy.value!!) && (binding.fMennyEdittext.text.toString() != ""))) || (binding.arEdittext.text.toString() != ""))) {
                val fmenny = when {
                    binding.kihagycheckBox.isChecked -> -1.0
                    binding.fMennyEdittext.text.toString() != "" -> binding.fMennyEdittext.text.toString()
                        .toDouble()
                    else -> binding.spajzNyersanyagModositViewModel!!.nyersanyag.value!!.F_Menny
                }
                val ar =
                    if (binding.arEdittext.text.toString() != "") binding.arEdittext.text.toString()
                        .toInt() else binding.spajzNyersanyagModositViewModel!!.nyersanyag.value!!.Ar
                binding.spajzNyersanyagModositViewModel!!.onModositButtonClicked(
                    binding.spajzNyersanyagModositViewModel!!.nyersanyag.value!!.ID,
                    fmenny, ar
                )
                binding.fMennyEdittext.setText("")
                binding.nyersanyagAtv.setText("")
                binding.arEdittext.setText("")
                binding.kihagycheckBox.isChecked = false
                binding.arTil.hint = "Ár"
                binding.fMennyTil.hint = "Figyelmeztetési mennyiség"
            } else {
                if (binding.arEdittext.text.toString() == "") {
                    binding.arTil.error = "Nem adtál meg új árat!"
                }
                if ((!binding.spajzNyersanyagModositViewModel!!.kihagy.value!!) && (binding.fMennyEdittext.text.toString() == "")) {
                    binding.fMennyTil.error = "Nem adtál meg új figyelmeztetési mennyiséget!"
                }
                if (!isCompleted) {
                    binding.nyersanyagTil.error = "Nincs beállítva helyesen a mező!"
                }
            }
        }
        return binding.root
    }
}