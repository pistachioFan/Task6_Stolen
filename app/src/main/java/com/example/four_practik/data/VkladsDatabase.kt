package com.example.four_practik.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Vklads::class], version = 1, exportSchema = false)
abstract class VkladsDatabase: RoomDatabase() {
    abstract fun vkladDao(): VkladDao
    companion object {
        @Volatile
        private var Instance : VkladsDatabase? = null
        fun getDatabase(context: Context): VkladsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context = context, VkladsDatabase::class.java, "vklad_database").build().also { Instance = it }
            }
        }
    }
}