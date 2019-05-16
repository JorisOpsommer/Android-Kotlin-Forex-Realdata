package com.joris.clientapp.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.joris.clientapp.models.LocalStorageStock


@Database(entities = arrayOf(LocalStorageStock::class), version = 1)
    abstract class LocalStorageStockDatabase : RoomDatabase() {

        abstract fun dao(): LocalStorageStockDao

        companion object {
            private var INSTANCE: LocalStorageStockDatabase? = null

            fun getInstance(context: Context): LocalStorageStockDatabase? {
                if (INSTANCE == null) {
                    synchronized(LocalStorageStockDatabase::class) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalStorageStockDatabase::class.java, "LocalStorageStocks_table")
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
                return INSTANCE
            }

            fun destroyInstance() {
                INSTANCE = null
            }
        }
    }
