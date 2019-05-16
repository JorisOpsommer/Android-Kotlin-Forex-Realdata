package com.joris.clientapp.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RetrofitClientInstance {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.iextrading.com/1.0/")
        .addConverterFactory(MoshiConverterFactory.create())
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
        .create(ApiService::class.java)

}



