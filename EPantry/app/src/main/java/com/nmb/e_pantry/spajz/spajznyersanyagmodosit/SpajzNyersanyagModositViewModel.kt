package com.nmb.e_pantry.spajz.spajznyersanyagmodosit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.database.Nyersanyag
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class SpajzNyersanyagModositViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _nyersanyag = MutableLiveData<Nyersanyag>()
    val nyersanyag: LiveData<Nyersanyag>
        get() = _nyersanyag

    private val _mertekegyseg = MutableLiveData<String>()
    val mertekegyseg: LiveData<String>
        get() = _mertekegyseg

    private val _kihagy = MutableLiveData<Boolean>()
    val kihagy: LiveData<Boolean>
        get() = _kihagy

    fun onStart(): ArrayList<String> {
        _kihagy.value = false
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

    fun onKihagyCBClicked(bool: Boolean){
        _kihagy.value = bool
    }

    fun onItemSelected(nev: String) {
        uiScope.launch {
            _nyersanyag.value = getNyersanyagData(nev)
        }
    }

    fun getMertekegyseg(id: Int) {
        uiScope.launch {
            _mertekegyseg.value = getME(id)
        }
    }


    fun onModositButtonClicked(id: Long, fmenny: Double, ar: Int) {
        uiScope.launch {
            modosit(id, fmenny, ar)
            _nyersanyag.value!!.F_Menny = fmenny
            _nyersanyag.value!!.Ar = ar
        }
    }

    private suspend fun modosit(id: Long, fmenny: Double, ar: Int) {
        withContext(Dispatchers.IO) {
            dataSource.updateNyersanyag(id, fmenny, ar)
        }
    }

    private suspend fun getNyersanyagData(nev: String): Nyersanyag {
        return withContext(Dispatchers.IO) {
            dataSource.getNyersanyag(dataSource.getNyersanyagId(nev))
        }
    }

    private suspend fun getME(id: Int): String {
        return withContext(Dispatchers.IO) {
            dataSource.getMertekFromId(id)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}