package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mertekegyseg")
data class Mertekegyseg(
    @PrimaryKey @ColumnInfo(name = "id")
    var ID: Int,
    @ColumnInfo(name = "nev")
    var nev: String
)