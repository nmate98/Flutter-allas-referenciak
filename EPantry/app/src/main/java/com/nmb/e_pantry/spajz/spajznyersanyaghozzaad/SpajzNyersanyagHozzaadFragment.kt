package com.nmb.e_pantry.spajz.spajznyersanyaghozzaad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nmb.e_pantry.R
import com.nmb.e_pantry.database.Nyersanyag
import com.nmb.e_pantry.database.PantryDatabase
import com.nmb.e_pantry.databinding.SpajzNyersanyagHozzaadFragmentBinding
import com.nmb.e_pantry.navView

class SpajzNyersanyagHozzaadFragment : Fragment() {
    lateinit var binding: SpajzNyersanyagHozzaadFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.spajz_nyersanyag_hozzaad_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = SpajzNyersanyagHozzadViewModelFactory(dataSource)
        val spajzNyersanyagHozzaadViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SpajzNyersanyagHozzaadViewModel::class.java)
        binding.spajzNyersanyagHozzaadViewModel = spajzNyersanyagHozzaadViewModel
        binding.fMennyTil.helperText =
            "Hagyd üresen, ha nem akarsz értesítést kapni erről a nyersanyagról a főoldalon."
        binding.hozzaadButton.setOnClickListener {
            if ((binding.nameEdittext.text.toString() != "") && (binding.mertekSpinner.selectedItemId != 0L) && (binding.arEdittext.text.toString() != "")) {
                binding.nameTil.error = null
                (binding.mertekSpinner.selectedView as TextView).error = null
                binding.fMennyTil.error = null
                binding.arTil.error = null
                if (binding.fMennyEdittext.text.toString() != "") {
                    binding.spajzNyersanyagHozzaadViewModel?.onHozzaadButtonPressed(
                        Nyersanyag(
                            0,
                            binding.nameEdittext.text.toString().capitalize(),
                            binding.mertekSpinner.selectedItemPosition,
                            binding.fMennyEdittext.text.toString().toDouble(),
                            Integer.parseInt(binding.arEdittext.text.toString())
                        )
                    )
                } else {
                    binding.spajzNyersanyagHozzaadViewModel?.onHozzaadButtonPressed(
                        Nyersanyag(
                            0,
                            binding.nameEdittext.text.toString().capitalize(),
                            binding.mertekSpinner.selectedItemPosition,
                            -1.0,
                            Integer.parseInt(binding.arEdittext.text.toString())
                        )
                    )
                }
                binding.nameEdittext.text?.clear()
                binding.arEdittext.text?.clear()
                binding.fMennyEdittext.text?.clear()
                binding.mertekSpinner.setSelection(0)
            } else {
                if (binding.arEdittext.text.toString() == "") {
                    binding.arTil.error = "Nem adtál meg árat!"
                }
                if (binding.nameEdittext.text.toString() == "") {
                    binding.nameTil.error = "Nem adtál meg nevet!"
                }
                if (binding.mertekSpinner.selectedItemId == 0L) {
                    (binding.mertekSpinner.getChildAt(0) as TextView).error = ""
                }
            }
        }
        return binding.root
    }
}