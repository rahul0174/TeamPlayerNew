package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.StateList
import kotlinx.android.synthetic.main.country_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class CountryListAdapter(val mContext: Context, var data : ArrayList<CountryList>, var listener:TextBookNow) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CountryListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<CountryList>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.country_list, viewGroup, false)
        return MyHolderView(v)
    }
    override fun onBindViewHolder(myHolderView: MyHolderView, position: Int) {
        myHolderView.bind(data.get(position),position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyHolderView(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(data: CountryList,position: Int) {
            itemView.tv_country.text=data.name
           /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
*/
            itemView.setOnClickListener{
                listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }
    fun filterList(filterdNames: java.util.ArrayList<CountryList>): java.util.ArrayList<CountryList> {
        this.data = filterdNames
        notifyDataSetChanged()
        return data as java.util.ArrayList<CountryList>
    }



}