package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupJoinDataItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_group_join_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.tv_group_list

class GroupJoinListAdapter (val mContext: Context, var data : java.util.ArrayList<GroupJoinDataItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<GroupJoinListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<GroupJoinDataItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_group_join_list, viewGroup, false)
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

        fun bind(data: GroupJoinDataItem, position: Int) {
            itemView.tv_in_adapter_group_join.text=data.name
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
                itemclick.onClickItem(position, MyConstants.GROUP_JOIN_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}