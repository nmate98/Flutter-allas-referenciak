package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "nyersanyag",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Mertekegyseg::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("m_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Nyersanyag(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID")
    var ID: Long,
    @ColumnInfo(name = "nev")
    var Nev: String,
    @ColumnInfo(name = "m_id", index = true)
    var M_ID: Int,
    @ColumnInfo(name = "figyelmeztetesi_mennyiseg")
    var F_Menny: Double,
    @ColumnInfo(name = "ar")
    var Ar: Int = 0
)
