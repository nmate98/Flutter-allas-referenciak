package com.nmb.e_pantry.spajz.spajzmegtekint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class SpajzMegtekintViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _spajz = MutableLiveData<List<RecViewData>>()
    val spajz: LiveData<List<RecViewData>>
        get() = _spajz

    fun onStart() {
        uiScope.launch {
            _spajz.value = getSpajz()
        }
    }

    private suspend fun getSpajz(): List<RecViewData> {
        return withContext(Dispatchers.IO) {
            var ki = ArrayList<RecViewData>()
            for (s in dataSource.getSpajzTartalom()) {
                ki.add(RecViewData(s.nyersanyag, s.mennyiseg.toString() + " " + s.mertekegyseg))
            }
            ki
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}