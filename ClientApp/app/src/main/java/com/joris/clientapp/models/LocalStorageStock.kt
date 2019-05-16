package com.joris.clientapp.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/**
 * This class is used to write data to our local database to store the users history
 */
   @Entity(tableName = "LocalStorageStocks_table")
    data class LocalStorageStock (

        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,

        val stockName: String,
        val date : String
    )
