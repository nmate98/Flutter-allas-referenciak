package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Nyersanyag::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("ny_id"),
            onDelete = ForeignKey.CASCADE
        )
    ), tableName = "bevasarlolista", primaryKeys = arrayOf("ny_id", "mennyiseg")
)
data class Bevasarlolista(
    @ColumnInfo(name = "ny_id")
    var NY_ID: Long,
    @ColumnInfo(name = "mennyiseg")
    var Mennyiseg: Double
)