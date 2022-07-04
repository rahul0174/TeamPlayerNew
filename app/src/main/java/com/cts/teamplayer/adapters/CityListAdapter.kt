package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CityList
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.StateList
import com.cts.teamplayer.network.ItemClickListner
import kotlinx.android.synthetic.main.country_list.view.*

class CityListAdapter (val mContext: Context, var data : ArrayList<CityList>, var itemclick: ItemClickListner,var listener: CityListAdapter.TextCityBookNow) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CityListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<CityList>
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

        fun bind(data: CityList, position: Int) {
            itemView.tv_country.text=data.name
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
            //    itemclick.onClickItem(position,4)
                   listener.citySet(position,data)
            }
        }

    }

    interface TextCityBookNow{
        fun citySet(position: Int,data: CityList)
    }
    fun filterList(filterdNames: java.util.ArrayList<CityList>): java.util.ArrayList<CityList> {
        this.data = filterdNames
        notifyDataSetChanged()
        return data as java.util.ArrayList<CityList>
    }



}