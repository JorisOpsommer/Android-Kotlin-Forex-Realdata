package com.joris.clientapp.data

import android.arch.lifecycle.MutableLiveData
import com.joris.clientapp.models.LocalStorageStock

class LocalStorageStockRepo private constructor(private val lsStockDao: LocalStorageStockDao) {

    private val stocklist = MutableLiveData<List<LocalStorageStock>>()

    /**
     * add a stock in db
     */
    fun addStock(stock: LocalStorageStock) {
                lsStockDao.insert(stock)
    }

    /**
     * Gets all the data in db. Reverse this list to show this chronological
     */
    fun getAllStocks(): MutableLiveData<List<LocalStorageStock>> {
        stocklist.postValue(getStocks().reversed())
        return stocklist
    }

    private fun getStocks() = lsStockDao.getAllSearches()

    /**
     * Deletes the whole list in db
     */
    fun deleteStocks() = lsStockDao.deleteAllSearches()

    /**
     * Can be used if we want to improve the history tab, now the list gets cleared. We could improve this
     * and only remove the first x items.
     */
    fun deleteSingleStock(lsStock: LocalStorageStock) = lsStockDao.deleteSingleSearch(lsStock.stockName)


    companion object {
        private var INSTANCE: LocalStorageStockRepo? = null

        fun getInstance(lsStockDao: LocalStorageStockDao): LocalStorageStockRepo {

            if (INSTANCE == null) {
                synchronized(LocalStorageStockRepo::class) {
                    INSTANCE = LocalStorageStockRepo(lsStockDao)
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


}