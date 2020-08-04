package com.nmb.e_pantry.spajz.spajznyersanyaghozzaad

import androidx.lifecycle.ViewModel
import com.nmb.e_pantry.database.Mennyiseg
import com.nmb.e_pantry.database.Nyersanyag
import com.nmb.e_pantry.database.PantryDatabaseDao
import kotlinx.coroutines.*

class SpajzNyersanyagHozzaadViewModel(private val dataSource: PantryDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun onHozzaadButtonPressed(sor: Nyersanyag) {
        uiScope.launch {
            insertIntoNyersanyag(sor)
            val id = getId(sor.Nev)
            insertIntoMennyiseg(Mennyiseg(id))
        }
    }

    fun onStart(): Array<String> {

        var data: Array<String> = Array(7) { "" }
        data[0] = "Válassz mértékegységet!"
        uiScope.launch {
            withContext(Dispatchers.IO) {
                for (i in 1..6) {
                    data[i] = getME()[i - 1]
                }
            }
        }
        return data
    }

    private suspend fun getME(): Array<String> {
        return withContext(Dispatchers.IO) {
            dataSource.getMertekegyseg()
        }
    }

    private suspend fun insertIntoNyersanyag(sor: Nyersanyag) {
        withContext(Dispatchers.IO) {
            dataSource.insertIntoNyersanyag(sor)
        }
    }

    private suspend fun getId(nev: String): Long {
        return withContext(Dispatchers.IO) {
            dataSource.getNyersanyagId(nev)
        }
    }

    private suspend fun insertIntoMennyiseg(mennyiseg: Mennyiseg) {
        withContext(Dispatchers.IO) {
            dataSource.insertIntoMennyiseg(mennyiseg)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}