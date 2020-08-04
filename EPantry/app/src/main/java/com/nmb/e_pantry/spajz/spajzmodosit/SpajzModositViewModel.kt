package com.nmb.e_pantry.spajz.spajzmodosit

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.database.Mennyiseg
import com.nmb.e_pantry.database.Nyersanyag
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class SpajzModositViewModel(
    private val dataSource: PantryDatabaseDao
) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _mertekegyseg = MutableLiveData<String>()
    val mertekegyseg: LiveData<String>
        get() = _mertekegyseg

    private val _id = MutableLiveData<Long>()
    val id: LiveData<Long>
        get() = _id

    fun onHozzaadButtonPressed(nev: String, mennyiseg: Double) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (dataSource.benneVanE(dataSource.getNyersanyagId(nev)) == 0) {
                    dataSource.insertIntoMennyiseg(
                        Mennyiseg(
                            dataSource.getNyersanyagId(nev),
                            mennyiseg
                        )
                    )
                } else {
                    var m = getMennyiseg(nev)
                    m += mennyiseg
                    updateMenny(nev, mennyiseg)
                }
            }
        }
    }

    fun onElveszButtonPressed(nev: String, mennyiseg: Double) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val m = getMennyiseg(nev)
                if (mennyiseg <= m) {
                    updateMenny(nev, -1 * mennyiseg)
                }
            }
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
            var m  = ""
            withContext(Dispatchers.IO) {
                m  = getME(nev)
            }
            _mertekegyseg.value = m
        }
    }

    private suspend fun getMEID(nev : String) : Int{
        return withContext(Dispatchers.IO){
            getNyersanyag(getNyersId(nev)).M_ID
        }
    }

    private suspend fun getME(nev: String): String {
        return withContext(Dispatchers.IO) {
            dataSource.getMertekFromId(getMEID(nev))
        }
    }

    private suspend fun getNyersanyag(id : Long) : Nyersanyag{
        return withContext(Dispatchers.IO){
            dataSource.getNyersanyag(id)
        }
    }

    private suspend fun getNyersId(nev: String): Long {
        return withContext(Dispatchers.IO) {
            dataSource.getNyersanyagId(nev)
        }
    }

    private suspend fun getMennyiseg(nev: String): Double {
        return withContext(Dispatchers.IO) {
            dataSource.getMennyiseg(getNyersId(nev))
        }
    }

    private suspend fun updateMenny(nev: String, mennyiseg: Double) {
        dataSource.updateMennyiseg(getNyersId(nev), mennyiseg)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}