package com.joris.clientapp.data

import android.arch.persistence.room.*
import com.joris.clientapp.models.LocalStorageStock

@Dao
interface LocalStorageStockDao {
    @Insert
    fun insert(stock: LocalStorageStock)

    @Update
    fun update(stock: LocalStorageStock)

    @Delete
    fun delete(stock: LocalStorageStock)

    @Query("Delete FROM LocalStorageStocks_table WHERE stockName = :stockName")
    fun deleteSingleSearch(stockName: String)

    @Query("DELETE FROM LocalStorageStocks_table")
    fun deleteAllSearches()

    @Query("SELECT * FROM LocalStorageStocks_table")
    fun getAllSearches(): MutableList<LocalStorageStock>
}