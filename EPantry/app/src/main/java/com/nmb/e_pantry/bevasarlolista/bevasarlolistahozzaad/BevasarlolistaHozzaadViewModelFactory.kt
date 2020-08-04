package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class BevasarlolistaHozzaadViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BevasarlolistaHozzaadViewModel::class.java)) {
            return BevasarlolistaHozzaadViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}