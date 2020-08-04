package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.Bevasarlolista
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class BevasarlolistaHozzaadViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _spajz = MutableLiveData<List<RecViewData>>()
    val spajz: LiveData<List<RecViewData>>
        get() = _spajz

    private val _bevlista = MutableLiveData<List<RecViewData>>()
    val bevlista: LiveData<List<RecViewData>>
        get() = _bevlista

    private val _mertekegyseg = MutableLiveData<String>()
    val mertekegyseg : LiveData<String>
    get()= _mertekegyseg

    fun onItemSelected(nev: String) {
        uiScope.launch {
            _mertekegyseg.value = getME(nev)
        }
    }

    fun onStart(): ArrayList<String> {
        var data: ArrayList<String> = ArrayList()
        data.add("Válassz nyersanyagot!")
        uiScope.launch {
            _spajz.value = getSpajz()
            _bevlista.value = getBev()
            withContext(Dispatchers.IO) {
                for (s in dataSource.getNyersanyag()) {
                    data.add(s)
                }
            }
        }
        return data
    }

    fun onButtonPressed(nev : String, mennyiseg : Double) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (dataSource.benneVanEABevlistaban(nev) == 1) {
                    dataSource.updateBevasarlolista(dataSource.getNyersanyagId(nev),mennyiseg)
                } else {
                    dataSource.insertIntoBevasarlolista(
                        Bevasarlolista(dataSource.getNyersanyagId(nev),mennyiseg)
                    )
                }
            }
            _bevlista.value = getBev()
        }
    }

    private suspend fun getBev(): List<RecViewData> {
        return withContext(Dispatchers.IO) {
            var ki = ArrayList<RecViewData>()
            for (s in dataSource.getBevasarlolista()) {
                ki.add(RecViewData(s.nyersanyag, s.mennyiseg.toString() + " " + s.mertekegyseg))
            }
            ki
        }
    }
    private suspend fun getME(nev: String): String {
        return withContext(Dispatchers.IO) {
            dataSource.getMertekFromId(dataSource.getNyersanyag(dataSource.getNyersanyagId(nev)).M_ID)
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