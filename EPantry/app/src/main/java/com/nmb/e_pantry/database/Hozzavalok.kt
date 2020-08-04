package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = arrayOf("r_id", "ny_id"),
    tableName = "Hozzavalok",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Receptek::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("r_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Nyersanyag::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("ny_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Hozzavalok(
    @ColumnInfo(name = "r_id", index = true)
    var R_ID: Long,
    @ColumnInfo(name = "ny_id", index = true)
    var NY_ID: Long,
    @ColumnInfo(name = "mennyiseg")
    var mennyiseg: Double
)