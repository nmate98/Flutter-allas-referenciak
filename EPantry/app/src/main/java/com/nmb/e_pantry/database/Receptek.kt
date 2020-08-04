package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Receptek")
data class Receptek(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var ID: Long,
    @ColumnInfo(name = "nev")
    var nev: String
)