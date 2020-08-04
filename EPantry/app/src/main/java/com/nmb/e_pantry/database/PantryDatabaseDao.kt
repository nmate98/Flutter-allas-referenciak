package com.nmb.e_pantry.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nmb.e_pantry.ReceptQueryResult
import com.nmb.e_pantry.SpajzQueryResult


@Dao
interface PantryDatabaseDao {
    @Insert
    fun insertIntoBevasarlolista(bevasarlolista: Bevasarlolista)

    @Insert
    fun insertIntoHozzavalok(hozzavalok: Hozzavalok)

    @Insert
    fun insertIntoMennyiseg(mennyiseg: Mennyiseg)

    @Insert
    fun insertIntoMertekegyseg(mertekegyseg: Mertekegyseg)

    @Insert
    fun insertIntoNyersanyag(nyersanyag: Nyersanyag)

    @Insert
    fun insertIntoReceptek(receptek: Receptek)

    @Delete
    fun deleteFromBevasarloLista(bevasarlolista: Bevasarlolista)

    @Query("Update nyersanyag set figyelmeztetesi_mennyiseg= :fmenny,ar  = :ar where ID = :id")
    fun updateNyersanyag(id: Long,fmenny : Double, ar : Int)

    @Query("Update mennyiseg set mennyiseg = (select mennyiseg from mennyiseg where ny_id = :id) + :mennyiseg where ny_id = :id")
    fun updateMennyiseg(id: Long, mennyiseg: Double)

    @Query("Update bevasarlolista set mennyiseg = (select mennyiseg from bevasarlolista where ny_id = :id) + :mennyiseg where ny_id = :id")
    fun updateBevasarlolista(id: Long, mennyiseg: Double)

    @Query("Select n.Nev as nyersanyag ,m.Mennyiseg,me.Nev as mertekegyseg from nyersanyag n  inner join mennyiseg m on n.ID = m.ny_id inner join mertekegyseg me on n.m_id = me.id where m.mennyiseg > 0 order by n.nev")
    fun getSpajzTartalom(): List<SpajzQueryResult>

    @Query("Select n.Nev as nyersanyag, h.Mennyiseg, m.Nev as mertekegyseg from Receptek r inner join hozzavalok h on r.id = h.r_id inner join nyersanyag n on h.ny_id = n.id inner join mertekegyseg m on n.m_id = m.id  where r.id = :k_id order by n.ID")
    fun getRecept(k_id: Long): List<ReceptQueryResult>

    @Query("Select n.Nev as nyersanyag, b.Mennyiseg, m.Nev as mertekegyseg from bevasarlolista b inner join nyersanyag n on b.ny_id = n.id inner join mertekegyseg m on n.m_id = m.id order by n.nev")
    fun getBevasarlolista(): List<SpajzQueryResult>

    @Query("Select Count(*) from mertekegyseg")
    fun getMertekCount(): Int

    @Query("Select nev from mertekegyseg")
    fun getMertekegyseg(): Array<String>

    @Query("Select mennyiseg from mennyiseg where ny_id = :id")
    fun getMennyiseg(id: Long): Double

    @Query("Select nev from nyersanyag order by nev")
    fun getNyersanyag(): List<String>

    @Query("Select Count(*) from mennyiseg where ny_id = :id")
    fun benneVanE(id: Long): Int

    @Query("Select nev from receptek order by nev")
    fun getReceptek(): List<String>

    @Query("Select id from receptek where nev = :nev")
    fun getReceptId(nev : String) : Long

    @Query("Select m. nev from mertekegyseg m inner join nyersanyag ny on m.id = ny.m_id")
    fun getAllMertek(): Array<String>

    @Query("Select max(id) from receptek")
    fun getReceptMaxId(): Long

    @Query("Select id from nyersanyag where nev = :nev")
    fun getNyersanyagId(nev: String): Long

    @Query("Select Count(ny.nev) from nyersanyag ny inner join bevasarlolista b on b.ny_id = ny.ID where ny.nev = :nev")
    fun benneVanEABevlistaban(nev: String): Int

    @Query("Select nev from nyersanyag where id = :id")
    fun getNyersFromId(id: Long): String

    @Query("Select * from bevasarlolista where ny_id = :id")
    fun getBevElem(id: Long): Bevasarlolista

    @Query("Select n.Nev as nyersanyag ,m.Mennyiseg,me.Nev as mertekegyseg from nyersanyag n  inner join mennyiseg m on n.ID = m.ny_id inner join mertekegyseg me on n.m_id = me.id where n.figyelmeztetesi_mennyiseg >= m.mennyiseg and n.figyelmeztetesi_mennyiseg >= 0 order by n.nev")
    fun getWarning(): List<SpajzQueryResult>

    @Query("Select sum(ny.ar*b.mennyiseg) from nyersanyag ny inner join bevasarlolista b on ny.ID = b.ny_id")
    fun getKoltseg(): Int

    @Query("Select * from nyersanyag where ID = :id")
    fun getNyersanyag(id : Long) : Nyersanyag

    @Query("Select nev from mertekegyseg where id = :id")
    fun getMertekFromId(id : Int) : String
}