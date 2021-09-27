package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants.INVITE_GROUP_LIST
import kotlinx.android.synthetic.main.country_list.view.*
import kotlinx.android.synthetic.main.country_list.view.tv_country
import kotlinx.android.synthetic.main.group_list.view.*

class GroupListAdapter (val mContext: Context, var data : java.util.ArrayList<GroupListItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<GroupListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<GroupListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.group_list, viewGroup, false)
        return MyHolderView(v)
    }
    override fun onBindViewHolder(myHolderView: MyHolderView, position: Int) {
        myHolderView.bind(data!!.get(position),position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class MyHolderView(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(data: GroupListItem, position: Int) {
            itemView.tv_group_list.text=data.name
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
                itemclick.onClickItem(position,INVITE_GROUP_LIST)
                //    listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}