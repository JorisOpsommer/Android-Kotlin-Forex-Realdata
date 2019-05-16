package com.joris.clientapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.joris.clientapp.data.LocalStorageStockRepo
import com.joris.clientapp.injection.base.BaseViewModel
import com.joris.clientapp.models.CompanyStock
import com.joris.clientapp.models.LocalStorageStock
import com.joris.clientapp.models.NameList
import com.joris.clientapp.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class
StockView(private val storageRepo: LocalStorageStockRepo) : BaseViewModel() {
    private lateinit var currentStock: MutableLiveData<CompanyStock>
    private lateinit var currentMostActive: MutableLiveData<MutableList<NameList>>
    private lateinit var currentGainers: MutableLiveData<MutableList<NameList>>
    private lateinit var currentAllStocksNamelist: MutableLiveData<MutableList<NameList>>
    private lateinit var stocksNameListRepo: MutableLiveData<MutableList<NameList>>

    private var searchValue: String = ""

    @Inject
    lateinit var stockApiService: ApiService

    private lateinit var subscription: Disposable

    /**
     * Uses loadDataSingleStock to update the currentStock liveData. Gets called when we want more info about one particular stock. (=quote)
     * @param stockSymbol Symbol used in forex, for example: aapl
     * @return return the stock in CompanyStock object
     */
    fun getSingleStockFromApi(stockSymbol: String): LiveData<CompanyStock> {
        if (!::currentStock.isInitialized) {
            currentStock = MutableLiveData()
        }
        loadDataSingleStock(stockSymbol)
        return currentStock
    }

    /**
     *Uses loadDataMostActive to update the currentMostActive List
     * @return return the list in Namelist object ready to be displayed with the namelistadapter
     */
    fun getMostActiveStocksFromApi(): LiveData<MutableList<NameList>> {
        if (!::currentMostActive.isInitialized) {
            currentMostActive = MutableLiveData()
        }
        loadDataMostActive()
        return currentMostActive
    }

    /**
     * Equal to getMostActiveStocksFromApi. But loads the gainers
     */
    fun getBestGainersFromApi(): LiveData<MutableList<NameList>> {
        if (!::currentGainers.isInitialized) {
            currentGainers = MutableLiveData()
        }
        loadDataBestGainers()
        return currentGainers
    }

    /**
     * Equal to getMostActiveStocksFromApi but gives a list of all the stocks in Forex according to our API
     */
    fun getAllStocksNamelistFromApi(): LiveData<MutableList<NameList>> {
        if (!::currentAllStocksNamelist.isInitialized) {
            currentAllStocksNamelist = MutableLiveData()
        }
        loadDataAllStocksNamelist()
        return currentAllStocksNamelist
    }

    /**
     * Sends a get request via the APIservice to get all the stocks in Forex according to our API
     * onError ==> send error
     * OnSucces ==> update currentAllStocksNamelist with result data
     */
    private fun loadDataAllStocksNamelist() {
        subscription = stockApiService.getNameList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveStocksNameListSucces(result) },
                { error -> onRetrieveStockError(error) }
            )
    }

    /**
     * Equals to loadDataAllStocksNamelist but gives the 24 hour most active stocks. This list can be empty!!
     */
    private fun loadDataMostActive() {
        subscription = stockApiService.getMostActive()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveStocksMostactiveSucces(result) },
                { error -> onRetrieveStockError(error) }
            )
    }

    /**
     * Equals to loadDataAllStocksNamelist but gives the 24 hour best gainers
     */
    private fun loadDataBestGainers() {
        subscription = stockApiService.getBestGainers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveStocksBestGainersSucces(result) },
                { error -> onRetrieveStockError(error) }
            )
    }

    /**
     *  Equals to loadDataAllStocksNamelist but gives onestock
     */
    private fun loadDataSingleStock(stockSymbol: String) {
        subscription = stockApiService.getStock(stockSymbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveStockSuccess(result) },
                { error -> onRetrieveStockError(error) }
            )
    }

    /**
     * update the currenstockList
     * @param result the correct data is passed to this method ==> now use this to set the currenstock attribute
     */
    private fun onRetrieveStockSuccess(result: CompanyStock) {
        currentStock.value = result
    }


    private fun onRetrieveStocksBestGainersSucces(result: MutableList<NameList>) {
        currentGainers.value = result
    }

    private fun onRetrieveStocksMostactiveSucces(result: MutableList<NameList>) {
        currentMostActive.value = result
    }

    private fun onRetrieveStocksNameListSucces(result: MutableList<NameList>) {
        currentAllStocksNamelist.value = result
    }

    /**
     * Print err in console
     */
    private fun onRetrieveStockError(error: Throwable) {
        println(error.message)
    }

    /**
     * Get all the stocks from our local database (Room)
     * Load them in via the loadstocks method.
     * @return stocksNameListRepo are all the stocks in Room
     */
    fun getStocksFromRepo(): LiveData<MutableList<NameList>> {
        stocksNameListRepo = MutableLiveData()
        loadStocks()
        return stocksNameListRepo
    }

    /**
     * Uses another thread so the application can move on while this method gets the data.
     * If the list is > 100, just clear it.
     */
    private fun loadStocks() {
        doAsync {
            val stocksFromRepo = storageRepo.getAllStocks()
            if (stocksFromRepo.value!!.size > 100) {
                removeStocksFromRepo()
            }

            var nList: MutableList<NameList> = mutableListOf()
            uiThread {
                stocksFromRepo.value!!.forEach {
                    nList.add(NameList(it.date, it.stockName, it.stockName))
                }
                stocksNameListRepo.value = nList
            }
        }

    }

    /**
     * Get the current date in Belgium format (day + month)
     * update the repo and pass the stock + date
     * @param stocksymbol pass the symbol in string for example: aapl
     */
    fun addSingleStocktoRepo(stocksymbol: String) {

        val day = SimpleDateFormat("dd")
        val month = SimpleDateFormat("MM")
        val currdate = day.format(Date()) + "/" + month.format(Date())

        doAsync {
            storageRepo.addStock(LocalStorageStock(stockName = stocksymbol, date = currdate))
            //deleteAndInsertSearch(Search(searchValue = city))
        }
    }

    /**
     * clear database
     */
    fun removeStocksFromRepo() {
        doAsync {
            storageRepo.deleteStocks()
        }
    }

    /**
     * Used to save the value in txtSearchStock EditTextField.
     * @return we return the saved data in this field.
     */
    fun getSearchValue(): String {
        return searchValue
    }

    /**
     * Setter to save the data in txtSearchStock
     */
    fun setSearchValue(search: String) {
        searchValue = search
    }


}