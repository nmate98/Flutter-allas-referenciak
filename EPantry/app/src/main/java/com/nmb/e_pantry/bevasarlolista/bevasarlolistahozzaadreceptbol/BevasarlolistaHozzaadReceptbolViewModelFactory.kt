package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaadreceptbol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class BevasarlolistaHozzaadReceptbolViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BevasarlolistaHozzaadReceptbolViewModel::class.java)) {
            return BevasarlolistaHozzaadReceptbolViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}