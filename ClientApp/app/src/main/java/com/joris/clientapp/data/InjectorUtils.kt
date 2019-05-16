package com.joris.clientapp.data

import android.content.Context
import com.joris.clientapp.viewmodel.ViewModelFactory

object InjectorUtils {

fun provideViewModelFactory(context: Context): ViewModelFactory {
    val stockrepo = LocalStorageStockRepo.getInstance(LocalStorageStockDatabase.getInstance(context)!!.dao())
    return ViewModelFactory(stockrepo)
}

    const val BASE_URL = "https://api.iextrading.com/1.0/"
}