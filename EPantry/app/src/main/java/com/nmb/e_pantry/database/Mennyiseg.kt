package com.nmb.e_pantry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mennyiseg",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Nyersanyag::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("ny_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Mennyiseg(
    @PrimaryKey @ColumnInfo(name = "ny_id", index = true)
    var NY_ID: Long,
    @ColumnInfo(name = "mennyiseg")
    var mennyiseg: Double = 0.0
)