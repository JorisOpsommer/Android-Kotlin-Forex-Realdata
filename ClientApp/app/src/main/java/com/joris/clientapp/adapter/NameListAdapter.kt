package com.joris.clientapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joris.clientapp.R
import com.joris.clientapp.models.NameList
import kotlinx.android.synthetic.main.stock_row.view.*

/**
 * Adapter to display every stock item that this app uses. We made it possible to convert every stock to this list.
 * This is done with the purpose to expand this app and add for example crypto, SPX, NDX to same list.
 */
class NameListAdapter(private var namelist: MutableList<NameList>, val clickListener: (NameList) -> Unit) :
    RecyclerView.Adapter<NameListAdapter.CustomViewHolder>() {


    /**
     * Number of items in the whole list
     */
    override fun getItemCount(): Int {
        return namelist.size
    }

    /**
     * creates a ViewHolder class and uses stock_row.xml as layoutfile
     */
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): CustomViewHolder {

        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stock_row, parent, false))
    }

    /**
     * bind namelist + clicklistener so we can use this in the class: CustomViewHolder
     * we need to bind the listener here so the fragment knows which stock is clicked.
     * @param holder Custom class we made where we will show the data
     * @position is necessary to loop al the objects
     *
     */
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        (holder).bind(namelist[position], clickListener)

    }

    /**
     *CustomView we made to display the list
     * @param namelist object is required to display it
     * @param clickListener required so we can set a clicklistener on the object we display
     */
    inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(nameList: NameList, clickListener: (NameList) -> Unit) {


            itemView.txtStockName.text = nameList.nameSymbol
            if (!nameList.nameCompany.isNullOrEmpty())
                itemView.txtCompanyName.text = nameList.nameCompany
            else
                itemView.txtCompanyName.text = nameList.name

            //clicklistener
            itemView.setOnClickListener { clickListener(nameList) }

        }
    }

    /**
     * Setter that is required to update the adapter
     * @param nl requirs a new list so we can replace the existing adapter with the new 'nl' list without the need to create a new adapter
     */
    fun setList(nl: MutableList<NameList>) {
        namelist.clear()
        namelist.addAll(nl)
    }

}