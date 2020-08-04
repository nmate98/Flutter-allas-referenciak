package com.nmb.e_pantry.bevasarlolista.bevasarlolistahozzaadreceptbol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.ReceptQueryResult
import com.nmb.e_pantry.SpajzQueryResult
import com.nmb.e_pantry.database.Bevasarlolista
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class BevasarlolistaHozzaadReceptbolViewModel(private val dataSource: PantryDatabaseDao) :
    ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _hianyzo = MutableLiveData<ArrayList<RecViewData>>()
    val hianyzo: LiveData<ArrayList<RecViewData>>
        get() = _hianyzo

    var hianyzotomb = ArrayList<Bevasarlolista>()

    private val _bevlista = MutableLiveData<ArrayList<RecViewData>>()
    val bevlista: LiveData<ArrayList<RecViewData>>
        get() = _bevlista

    lateinit var bevlistatomb: List<SpajzQueryResult>

    init {
        _hianyzo.value = ArrayList()
        _bevlista.value = ArrayList()
    }


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


    fun onKivalasztButtonPressed(nev : String) {
        _hianyzo.value!!.clear()
        hianyzotomb.clear()
        uiScope.launch {
            var id = getReceptId(nev)
            var hozzavalok = getHozzavalo(id)
            for (h in hozzavalok) {
                if (getNyersanyagMennyiseg(getNyId(h.nyersanyag)) < h.mennyiseg) {
                    hianyzotomb.add(
                        Bevasarlolista(
                            getNyId(h.nyersanyag),
                            (h.mennyiseg - getNyersanyagMennyiseg(getNyId(h.nyersanyag)))
                        )
                    )
                }
            }
            val ki = ArrayList<RecViewData>()
            for (h in hozzavalok) {
                if (getNyersanyagMennyiseg(getNyId(h.nyersanyag)) < h.mennyiseg) {
                    ki.add(
                        RecViewData(
                            h.nyersanyag, (h.mennyiseg - getNyersanyagMennyiseg(
                                getNyId(h.nyersanyag)
                            )).toString() + " " + h.mertekegyseg
                        )
                    )
                }
            }
            _hianyzo.value = ki
        }
    }

    fun onHozzaadButtonPressed() {
        uiScope.launch {
            for (h in hianyzotomb) {
                withContext(Dispatchers.IO) {
                    if (dataSource.benneVanEABevlistaban(dataSource.getNyersFromId(h.NY_ID)) == 1) {
                        for (b in bevlistatomb) {
                            if (b.nyersanyag == dataSource.getNyersFromId(h.NY_ID)) {
                                dataSource.updateBevasarlolista(h.NY_ID, h.Mennyiseg)
                            }
                        }
                    } else {
                        dataSource.insertIntoBevasarlolista(
                            Bevasarlolista(
                                h.NY_ID,
                                h.Mennyiseg
                            )
                        )
                    }
                }
                _bevlista.value = ArrayList()
                getBevLista()
            }
        }
    }


    fun getBevLista() {
        uiScope.launch {
            bevlistatomb = getListaTomb()
            _bevlista.value = getListaString()
        }
    }

    private suspend fun getReceptId(nev : String) : Long{
        return withContext(Dispatchers.IO){
            dataSource.getReceptId(nev)
        }
    }

    private suspend fun getHozzavalo(id: Long): List<ReceptQueryResult> {
        return withContext(Dispatchers.IO) {
            var h = dataSource.getRecept(id)
            h
        }
    }

    private suspend fun getNyersanyagMennyiseg(id: Long): Double {
        return withContext(Dispatchers.IO) {
            var mennyiseg = dataSource.getMennyiseg(id)
            mennyiseg
        }
    }

    private suspend fun getNyId(nev: String): Long {
        return withContext(Dispatchers.IO) {
            dataSource.getNyersanyagId(nev)
        }
    }

    private suspend fun getListaTomb(): ArrayList<SpajzQueryResult> {
        return withContext(Dispatchers.IO) {
            val ki: ArrayList<SpajzQueryResult> = ArrayList()
            for (b in dataSource.getBevasarlolista()) {
                ki.add(b)
            }
            ki
        }
    }

    private suspend fun getListaString(): ArrayList<RecViewData> {
        return withContext(Dispatchers.IO) {
            var ki = ArrayList<RecViewData>()
            for (s in dataSource.getBevasarlolista()) {
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