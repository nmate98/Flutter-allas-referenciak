package com.nmb.e_pantry.recept.receptmegtekint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class ReceptMegtekintViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _recept = MutableLiveData<List<RecViewData>>()
    val recept: LiveData<List<RecViewData>>
        get() = _recept

    fun onStart(): ArrayList<String> {
        var data: ArrayList<String> = ArrayList()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                for (s in dataSource.getReceptek()) {
                    data.add(s)
                }
            }
        }
        return data
    }

    fun onButtonClicked(nev: String) {
        uiScope.launch {
            _recept.value = getRecept(nev)
        }
    }

    private suspend fun getRecept(nev: String): List<RecViewData> {
        return withContext(Dispatchers.IO) {
            var ki = ArrayList<RecViewData>()
            println(dataSource.getRecept(dataSource.getReceptId(nev)))
            for (s in dataSource.getRecept(dataSource.getReceptId(nev))) {
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