package com.nmb.e_pantry.spajz.spajznyersanyaghozzaad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class SpajzNyersanyagHozzadViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpajzNyersanyagHozzaadViewModel::class.java)) {
            return SpajzNyersanyagHozzaadViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}