package com.joris.clientapp.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joris.clientapp.MainActivity
import com.joris.clientapp.R
import com.joris.clientapp.data.InjectorUtils
import com.joris.clientapp.models.CompanyStock
import com.joris.clientapp.viewmodel.StockView
import com.joris.clientapp.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_onestock.view.*

class OneStockFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: StockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideViewModelFactory(this.context!!)
        viewModel = ViewModelProviders.of(activity!!, factory).get(StockView::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View {
        val rootview = inflater.inflate(
            R.layout.fragment_onestock,
            container,
            false
        )
        loadSingleStock(rootview)
        refreshlistener(rootview)
        return rootview
    }

    /**
     * Checks the bundle to get the stock symbol. Now the view can give us more info about that symbol.
     * We check if the symbol matches the response stock symbol. This is needed because when internet connection fails, the used might click on another stock
     * and thus the view will respond with an empty list and the user will get to see the last clicked stock when there was internet.
     * Therefor this if makes sure the user will see nothing or the correct stock even when internet connection fails
     */
    private fun loadSingleStock(rootview: View?) {
        rootview!!.loadingPanel3.setVisibility(View.VISIBLE);
        var singleStock = arguments!!.getString("namesymbol")
        viewModel.getSingleStockFromApi(singleStock).observe(this, Observer<CompanyStock> { stock ->
            (activity as MainActivity).getIdlingResourceInTest().increment()
            if (singleStock.toLowerCase().trim().equals(stock!!.datastock.nameSymbol.toLowerCase().trim())) {
                rootview.txtOneStockWelcome.text = "More info about " + stock.datastock.nameSymbol
                rootview.txtOneStockCompanyFill.text = stock.datastock.nameCompany
                rootview.txtOneStockSymbolFill.text = stock.datastock.nameSymbol
                rootview.txtOneStockPriceFill.text = stock.datastock.currentPrice.toString()
                rootview.txtOneStockHighFill.text = " " + stock.datastock.high.toString()
                rootview.txtOneStockLowFill.text = stock.datastock.low.toString()
                rootview.txtOneStockVolumeFill.text = stock.datastock.volume.toString()
                rootview.txtOneStockAvgVolumeFill.text = stock.datastock.avgVolume.toString()
                rootview.txtOneStockExchangeFill.text = stock.datastock.primaryExchange
                rootview.txtOneStockNasdaqUrl.text = "https://www.nasdaq.com/symbol/" + stock.datastock.nameSymbol +
                        "/real-time"
            }
            (activity as MainActivity).getIdlingResourceInTest().decrement()
        })
        rootview!!.loadingPanel3.setVisibility(View.GONE);
    }

    private fun refreshlistener(rootview: View?) {
        rootview!!.swipeRefreshLayoutOneStock.setOnRefreshListener {
            loadSingleStock(rootview)
            rootview.swipeRefreshLayoutOneStock.isRefreshing = false

        }
    }


}





