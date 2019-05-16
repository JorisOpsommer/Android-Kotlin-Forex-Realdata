package com.joris.clientapp.network


import com.joris.clientapp.models.CompanyStock
import com.joris.clientapp.models.NameList
import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.Observable

/**
 * Calls that are done to the API
 */
interface ApiService {

    /**
     * Gets more info about one stock (=quote)
     */
    @GET("stock/{code}/book")
    fun getStock (@Path("code") code : String): Observable<CompanyStock>

    /**
     * Gives us the list of all the forex stocks in the API
     */
    @GET("ref-data/symbols")
    fun getNameList (): Observable<MutableList<NameList>>

    /**
     * Gives us the most active stocks in the last 24hours. Can be empty when market is not active!
     */
    @GET("stock/market/list/mostactive")
    fun getMostActive (): Observable<MutableList<NameList>>

    /**
     *Gives us best 24 hour gainers in the market in the last 24hours. Never empty!
     */
    @GET("stock/market/list/gainers")
    fun getBestGainers (): Observable<MutableList<NameList>>
}