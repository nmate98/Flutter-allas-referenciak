package com.nmb.e_pantry.recept.recepthozzaad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.Hozzavalok
import com.nmb.e_pantry.database.PantryDatabaseDao
import com.nmb.e_pantry.database.Receptek
import kotlinx.coroutines.*

class ReceptHozzaadViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _mertekegyseg = MutableLiveData<String>()
    val mertekegyseg: LiveData<String>
        get() = _mertekegyseg

    private var max = 0L

    private val _recept = MutableLiveData<ArrayList<RecViewData>>()
    val recept: LiveData<ArrayList<RecViewData>>
        get() = _recept

    val receptTomb = ArrayList<Hozzavalok>()

    init {
        _recept.value = ArrayList()
    }

    fun addRecept(recept: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.insertIntoReceptek(Receptek(0, recept))
                max = dataSource.getReceptMaxId()
                for (h in receptTomb) {
                    h.R_ID = max
                    dataSource.insertIntoHozzavalok(h)
                }
            }
            receptTomb.clear()
            _recept.value = ArrayList()
        }
    }


    fun onStart(): ArrayList<String> {
        var data: ArrayList<String> = ArrayList()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                for (s in dataSource.getNyersanyag()) {
                    data.add(s)
                }
            }
        }
        return data
    }

    fun onItemSelected(nev: String) {
        uiScope.launch {
            _mertekegyseg.value = getME(nev)
        }
    }

    fun onHozzaadButtonClicked(hnev: String,mennyiseg : Double, hmert: String) {
        uiScope.launch {
            val id = getNyersId(hnev)
            var hely = -1
            val hv = _recept.value!!
            for (i in 0 until receptTomb.size) {
                if (receptTomb[i].NY_ID == id) {
                    hely = i
                }
            }
            if (hely != -1) {
                receptTomb[hely].mennyiseg = receptTomb[hely].mennyiseg + mennyiseg
                val menny = _recept.value!![hely].menny.split(" ")
                val recViewData =
                    RecViewData(hnev, (menny[0].toDouble() + mennyiseg).toString() + " " + menny[1])
                hv[hely] = recViewData
            } else {
                receptTomb.add(Hozzavalok(0,id, mennyiseg))
                hv.add(RecViewData(hnev, "$mennyiseg $hmert"))
            }
            _recept.value = hv
        }

    }

    fun onTorolButtonClicked() {
        val hv = _recept.value!!
        receptTomb.removeAt(receptTomb.size - 1)
        hv.removeAt(_recept.value!!.size - 1)
        _recept.value = hv
    }

    private suspend fun getME(nev: String): String {
        return withContext(Dispatchers.IO) {
            dataSource.getMertekFromId(dataSource.getNyersanyag(dataSource.getNyersanyagId(nev)).M_ID)
        }
    }

    private suspend fun getNyersId(nev : String) : Long{
        return withContext(Dispatchers.IO){
            dataSource.getNyersanyagId(nev)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}