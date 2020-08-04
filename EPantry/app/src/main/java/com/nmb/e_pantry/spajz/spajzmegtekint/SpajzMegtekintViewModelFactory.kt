package com.nmb.e_pantry.spajz.spajzmegtekint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmb.e_pantry.database.PantryDatabaseDao

class SpajzMegtekintViewModelFactory(private val dataSource: PantryDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpajzMegtekintViewModel::class.java)) {
            return SpajzMegtekintViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}