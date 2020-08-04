package com.nmb.e_pantry.spajz.spajznyersanyagmodosit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class SpajzNyersanyagModositViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpajzNyersanyagModositViewModel::class.java)) {
            return SpajzNyersanyagModositViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}