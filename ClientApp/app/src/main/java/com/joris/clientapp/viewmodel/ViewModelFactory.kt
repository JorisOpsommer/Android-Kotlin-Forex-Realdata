package com.joris.clientapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.joris.clientapp.data.LocalStorageStockRepo

class ViewModelFactory (private val stockrepo: LocalStorageStockRepo) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
override fun <T : ViewModel?> create(modelclass : Class<T>): T {
        return StockView(stockrepo) as T
    }


}