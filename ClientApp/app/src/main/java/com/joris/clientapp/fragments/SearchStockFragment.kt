package com.joris.clientapp.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_searchstocks.*
import kotlinx.android.synthetic.main.fragment_searchstocks.view.*


class SearchStockFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: StockView
    private lateinit var namelistAdapter: NameListAdapter


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
            R.layout.fragment_searchstocks,
            container,
            false
        )
        loadRecycleview(rootview)
        textChangeListener(rootview)
        refreshlistener(rootview)
        return rootview
    }

    /**
     * Loads all the stocks in the recycleview and uses the variable: namelistAdapter this is important because in the method: reloadRecycleview
     * we want to use the same adapter to use best practices
     */
    private fun loadRecycleview(rootview: View) {

        rootview!!.loadingPanel2.setVisibility(View.VISIBLE);
        viewModel.getAllStocksNamelistFromApi().observe(this, Observer<MutableList<NameList>> { stock ->
            (activity as MainActivity).getIdlingResourceInTest().increment()
            recyclerviewNameList.layoutManager = LinearLayoutManager(activity)

            namelistAdapter = NameListAdapter(stock!!, { namelist: NameList -> nameListItemClicked(namelist) })

            rootview.recyclerviewNameList.adapter = namelistAdapter
            rootview!!.loadingPanel2.setVisibility(View.GONE);
            (activity as MainActivity).getIdlingResourceInTest().decrement()
        })
    }

    /**
     * Adds a textlistener to the txtSearchStock EditTextfield.
     * Everytime the text changes, call reloadrecycleview method
     */
    private fun textChangeListener(rootview: View) {

        rootview.txtSearchStock.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                reloadRecycleview(rootview, s)
            }

        })
    }

    /**
     * Gets called when textChangeListener detects a change.
     * @param s is the full string in txtSearchStock EditTextField.
     * When called we reload the stock list and filter the stocks that match the text in txtSearchStock.
     * We fill the list: nList and use this to call the method: setList in the existing nameadapter
     * We notify that the data is changed.
     */
    fun reloadRecycleview(rootview: View, s: CharSequence) {

        viewModel.getAllStocksNamelistFromApi().observe(this, Observer<MutableList<NameList>> { stock ->
            (activity as MainActivity).getIdlingResourceInTest().increment()

            var nList: MutableList<NameList> = mutableListOf()
            stock!!.forEach { st ->

                if (
                    st.nameCompany.toLowerCase().trim().contains(s.toString().toLowerCase()) && !s.toString().isEmpty() || st.nameSymbol.toLowerCase().trim().contains(
                        s.toString().toLowerCase()
                    ) && !s.toString().isEmpty()
                ) {
                    if (!nList.contains(st))
                        nList.add(st)
                }
            }
            namelistAdapter.setList(nList)
            namelistAdapter.notifyDataSetChanged()
            (activity as MainActivity).getIdlingResourceInTest().decrement()
        })


    }


    private fun nameListItemClicked(namelist: NameList) {
        val bundle = Bundle()
        bundle.putString("namesymbol", namelist.nameSymbol)
        viewModel.addSingleStocktoRepo(namelist.nameSymbol)
        (activity as MainActivity).changeFragment(OneStockFragment(), bundle, "onestock", true)
    }

    private fun refreshlistener(rootview: View?) {
        rootview!!.swipeRefreshLayoutSearch.setOnRefreshListener {
            loadRecycleview(rootview)
            rootview.swipeRefreshLayoutSearch.isRefreshing = false
        }

    }

    /**
     * When this fragment gets destroyed we write the current string in txtSearchStock to the Viewmodel.
     * This is needed to save the data when the user rotates his/her screen.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setSearchValue(txtSearchStock.text.toString())
    }

    /**
     * When this fragment is created we check in the Viewmodel if there is an existing string for txtSearchStock.
     * Load the string in the EditTextField so the user does not lose its data.
     */
    override fun onResume() {
        super.onResume()
        if (viewModel.getSearchValue().isNotEmpty() || viewModel.getSearchValue() != "")
            view!!.txtSearchStock.setText(viewModel.getSearchValue())
    }

}