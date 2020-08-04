package com.nmb.e_pantry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Bevasarlolista::class, Hozzavalok::class, Receptek::class, Mertekegyseg::class, Mennyiseg::class, Nyersanyag::class],
    version = 1,
    exportSchema = false
)
abstract class PantryDatabase : RoomDatabase() {
    abstract val pantryDatabaseDao: PantryDatabaseDao


    companion object {
        @Volatile
        private var INSTANCE: PantryDatabase? = null

        fun getInstance(context: Context): PantryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PantryDatabase::class.java,
                        "bevasarlolista"
                    ).fallbackToDestructiveMigration().build()
                }
                INSTANCE = instance
                return instance
            }

        }
    }
}