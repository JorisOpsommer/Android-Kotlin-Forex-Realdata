package com.joris.clientapp.injection.base

import android.arch.lifecycle.ViewModel
import com.joris.clientapp.injection.component.DaggerViewModelInjector
import com.joris.clientapp.injection.component.ViewModelInjector
import com.joris.clientapp.injection.module.NetworkModule
import com.joris.clientapp.viewmodel.StockView


abstract class BaseViewModel: ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is StockView  -> injector.inject(this)
            //is LocalStorageView -> injector.inject(this)
        }
    }

    }