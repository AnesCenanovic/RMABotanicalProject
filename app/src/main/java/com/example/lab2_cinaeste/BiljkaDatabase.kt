package com.example.lab2_cinaeste

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [Biljka::class,BiljkaBitmap::class], version = 2)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO


    companion object {

        private var INSTANCE: BiljkaDatabase? = null
        fun getInstance(context: Context): BiljkaDatabase {
            if (INSTANCE == null) {
                synchronized(BiljkaDatabase::class) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }
        private const val DATABASE_NAME = "biljke-db"

        fun buildDatabase(context: Context): BiljkaDatabase {
            return Room.databaseBuilder(context.applicationContext, BiljkaDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

}
