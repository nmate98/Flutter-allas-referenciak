package com.nmb.e_pantry.bevasarlolista.bevasarlolistamegtekint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class BevasarlolistaMegtekintViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BevasarlolistaMegtekintViewModel::class.java)) {
            return BevasarlolistaMegtekintViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}