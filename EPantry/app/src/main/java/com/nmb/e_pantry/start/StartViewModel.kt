package com.nmb.e_pantry.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.database.Mertekegyseg
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class StartViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _warning = MutableLiveData<List<RecViewData>>()
    val warning: LiveData<List<RecViewData>>
        get() = _warning

    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id

    fun onStart() {
        uiScope.launch {
            insert()
        }
        getWarning()
    }

    private suspend fun insert() {
        withContext(Dispatchers.IO) {
            val count = dataSource.getMertekCount()
            if (count == 0) {
                dataSource.insertIntoMertekegyseg(Mertekegyseg(1, nev = "kg"))
                dataSource.insertIntoMertekegyseg(Mertekegyseg(2, nev = "l"))
                dataSource.insertIntoMertekegyseg(Mertekegyseg(3, nev = "db"))
                dataSource.insertIntoMertekegyseg(Mertekegyseg(4, nev = "csomag"))
                dataSource.insertIntoMertekegyseg(Mertekegyseg(5, nev = "dkg"))
                dataSource.insertIntoMertekegyseg(Mertekegyseg(6, nev = "g"))
            }
        }
    }

    fun getWarning() {
        uiScope.launch {
            _warning.value = getAllWarning()
        }
    }

    fun onClickListener(id: String) {
        _id.value = id
    }

    private suspend fun getAllWarning(): List<RecViewData> {
        return withContext(Dispatchers.IO) {
            var ki = ArrayList<RecViewData>()
            for (w in dataSource.getWarning()) {
                ki.add(RecViewData(w.nyersanyag, w.mennyiseg.toString() + " " + w.mertekegyseg))
            }
            ki
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}