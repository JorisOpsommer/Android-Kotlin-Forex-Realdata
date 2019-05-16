package com.joris.clientapp.injection.component

import com.joris.clientapp.injection.module.NetworkModule
import com.joris.clientapp.viewmodel.StockView
import dagger.Component
import javax.inject.Singleton



    @Singleton
    @Component(modules = [NetworkModule::class])
    interface ViewModelInjector {

        fun inject(stockviewModel: StockView)

        @Component.Builder
        interface Builder {
            fun build(): ViewModelInjector

            fun networkModule(networkModule: NetworkModule): Builder
        }

    }


