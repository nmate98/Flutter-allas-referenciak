package com.nmb.e_pantry.spajz.spajzmegtekint

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
import com.nmb.e_pantry.databinding.SpajzMegtekintFragmentBinding
import com.nmb.e_pantry.navView
import com.nmb.e_pantry.recyclerviewadapters.SpajzRecyclerviewAdapter

class SpajzMegtekintFragment : Fragment() {
    lateinit var binding: SpajzMegtekintFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.spajz_megtekint_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PantryDatabase.getInstance(application).pantryDatabaseDao
        val viewModelFactory = SpajzMegtekintViewModelFactory(dataSource)
        val spajzMegtekintViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SpajzMegtekintViewModel::class.java)
        binding.spajzMegtekintViewModel = spajzMegtekintViewModel

        binding.spajzMegtekintViewModel?.onStart()

        val adapter = SpajzRecyclerviewAdapter()
        binding.spajzRecyclerview.adapter = adapter
        val manager = GridLayoutManager(this.activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.spajzRecyclerview.layoutManager = manager

        binding.spajzMegtekintViewModel?.spajz!!.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }
}