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
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {

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
            R.layout.fragment_history,
            container,
            false
        )
        loadhistory()
        refreshlistener(rootview)
        return rootview
    }

    /**
     * This method loads the history in, data is get from the StockView class which uses Room to acces this local data
     * show the data via the NamelistAdapter
     */
    private fun loadhistory() {

        viewModel.getStocksFromRepo().observe(this, Observer<MutableList<NameList>> { stock ->
            recycleviewHistory.layoutManager = LinearLayoutManager(activity)
            recycleviewHistory.adapter =
                    NameListAdapter(stock!!, { namelist: NameList -> nameListItemClicked(namelist) })
        })
    }

    /**
     * Listener when an item is clicked from the namelistadapter, this method is called. If this item is clicked, go to onestockfragment and give more info about the clicked item (stock)
     * @param namelist requires the object to add this to the history and bundle
     */
    private fun nameListItemClicked(namelist: NameList) {
        val bundle = Bundle()
        bundle.putString("namesymbol", namelist.nameCompany)
        viewModel.addSingleStocktoRepo(namelist.nameCompany)
        (activity as MainActivity).changeFragment(OneStockFragment(), bundle, "onestock", true)
    }

    /**
     *SwipeToRefresh calls this method. Refresh the recycleview
     */
    private fun refreshlistener(rootview: View?) {
        rootview!!.swipeRefreshLayoutHistory.setOnRefreshListener {
            loadhistory()
            rootview.swipeRefreshLayoutHistory.isRefreshing = false

        }
    }

    override fun onResume() {
        super.onResume()
        loadhistory()
    }
}