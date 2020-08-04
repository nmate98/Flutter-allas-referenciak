package com.nmb.e_pantry.bevasarlolista.bevasarlolistamegtekint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class BevasarlolistaMegtekintViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _bevlista = MutableLiveData<List<RecViewData>>()
    val bevlista: LiveData<List<RecViewData>>
        get() = _bevlista
    private val _koltseg = MutableLiveData<Int>()
    val koltseg: LiveData<Int>
        get() = _koltseg
    val koltsegString: LiveData<String> = Transformations.map(koltseg) { transform(koltseg) }

    init {
        _koltseg.value = 0
    }

    private fun transform(k: LiveData<Int>): String {
        return "Összesen: ${k.value} Ft."
    }

    fun onStart() {
        uiScope.launch {
            _bevlista.value = getBev()
        }
    }

    fun deleteItem(nev: String) {
        uiScope.launch {
            deleteData(nev)
            getOsszKoltseg()
        }

    }

    fun getOsszKoltseg() {
        uiScope.launch {
            _koltseg.value = getKoltseg()
        }
    }

    private suspend fun getBev(): List<RecViewData> {
        return withContext(Dispatchers.IO) {
            val ki = ArrayList<RecViewData>()
            for (s in dataSource.getBevasarlolista()) {
                ki.add(RecViewData(s.nyersanyag, s.mennyiseg.toString() + " " + s.mertekegyseg))
            }
            ki
        }
    }

    private suspend fun deleteData(nev: String) {
        withContext(Dispatchers.IO) {
            val id = dataSource.getNyersanyagId(nev)
            dataSource.deleteFromBevasarloLista(dataSource.getBevElem(id))
        }
        onStart()
    }



    private suspend fun getKoltseg(): Int {
        return withContext(Dispatchers.IO) {
            val ki = dataSource.getKoltseg()
            ki
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}