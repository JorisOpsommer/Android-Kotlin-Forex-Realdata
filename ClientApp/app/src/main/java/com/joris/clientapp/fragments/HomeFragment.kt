package com.joris.clientapp.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joris.clientapp.MainActivity
import com.joris.clientapp.R
import com.joris.clientapp.adapter.NameListAdapter
import com.joris.clientapp.data.InjectorUtils
import com.joris.clientapp.models.NameList
import com.joris.clientapp.viewmodel.StockView
import com.joris.clientapp.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {


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
            R.layout.fragment_home,
            container,
            false
        )
        fillStocks(rootview)
        refreshlistener(rootview)
        return rootview
    }

    /**
     * Loads the most active stocks in the last 24 hour in the home recycleview. This method checks if the response is empty.
     * When the response is empty this means that the market is closed so we will load a new method: loadBestGainers.
     * We could always load the best gainers but the purpose of this app is to give the user live data. Meaning this data is more useful for the user than loadBestGainers
     * @param rootview we pass the view so we can acces data in the xml.
     */
    private fun fillStocks(rootview: View) {

        rootview!!.loadingPanel.setVisibility(View.VISIBLE);
        viewModel.getMostActiveStocksFromApi().observe(this, Observer<MutableList<NameList>> { stock ->
            (activity as MainActivity).getIdlingResourceInTest().increment()
            if (!stock!!.size.equals(0)) {

                recycleviewMostactive.layoutManager = LinearLayoutManager(activity)
                recycleviewMostactive.adapter =
                        NameListAdapter(stock!!, { namelist: NameList -> nameListItemClicked(namelist) })
                rootview!!.loadingPanel.setVisibility(View.GONE);
            } else {
                rootview.txtmostactive.text = "Market is closed, showing latest 24hour gainers"
                loadBestGainers(rootview)
            }
            (activity as MainActivity).getIdlingResourceInTest().decrement()

        })

    }

    /**
     * This method gets called occasionally. Only when fillstocks returns an empty list. This method loads the last 24hour best market gainers of the Forex market.
     */
    private fun loadBestGainers(rootview: View) {

        viewModel.getBestGainersFromApi().observe(this, Observer<MutableList<NameList>> { stock ->
            (activity as MainActivity).getIdlingResourceInTest().increment()
            if (!stock!!.size.equals(0)) {

                recycleviewMostactive.layoutManager = LinearLayoutManager(activity)
                recycleviewMostactive.adapter =
                        NameListAdapter(stock!!, { namelist: NameList -> nameListItemClicked(namelist) })
                rootview!!.loadingPanel.setVisibility(View.GONE);
            }
            (activity as MainActivity).getIdlingResourceInTest().decrement()
        })
    }

    /**
     * Listener when an item is clicked from the namelistadapter, this method is called. If this item is clicked, go to onestockfragment and give more info about the clicked item (stock)
     * @param namelist requires the object to add this to the history and bundle
     */

    private fun nameListItemClicked(namelist: NameList) {
        val bundle = Bundle()
        bundle.putString("namesymbol", namelist.nameSymbol)
        viewModel.addSingleStocktoRepo(namelist.nameSymbol)
        (activity as MainActivity).changeFragment(OneStockFragment(), bundle, "onestock", true)
    }

    /**
     * SwipeToRefresh calls this method. Refresh the recycleview
     */
    private fun refreshlistener(rootview: View?) {
        rootview!!.swipeRefreshLayoutHome.setOnRefreshListener {
            fillStocks(rootview)
            rootview.swipeRefreshLayoutHome.isRefreshing = false

        }

    }

}



