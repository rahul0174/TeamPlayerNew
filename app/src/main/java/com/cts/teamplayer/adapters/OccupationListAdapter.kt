package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.OccupationsList
import com.cts.teamplayer.models.SectorList
import com.cts.teamplayer.network.ItemClickListner
import kotlinx.android.synthetic.main.country_list.view.*

class OccupationListAdapter(val mContext: Context, var data : ArrayList<OccupationsList>, var itemclick: ItemClickListner,val listener:TextBookOccupationNow) :
    androidx.recyclerview.widget.RecyclerView.Adapter<OccupationListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<OccupationsList>
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

        fun bind(data: OccupationsList, position: Int) {
            itemView.tv_country.text=data.name
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
              //  itemclick.onClickItem(position,2)
                    listener.bookSession(position,data)
            }
        }

    }
    fun filterList(filterdNames: java.util.ArrayList<OccupationsList>): java.util.ArrayList<OccupationsList> {
        this.data = filterdNames
        notifyDataSetChanged()
        return data as java.util.ArrayList<OccupationsList>
    }

    interface TextBookOccupationNow{
        fun bookSession(position: Int,data: OccupationsList)
    }



}