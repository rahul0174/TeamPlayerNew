package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.Group
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.PendingJoinGroupDataItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.adapter_pending_group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.tv_group_list

class PendingGroupListAdapter (val mContext: Context, var data : java.util.ArrayList<PendingJoinGroupDataItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<PendingGroupListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<GroupListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_pending_group_list, viewGroup, false)
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

        fun bind(data: PendingJoinGroupDataItem, position: Int) {


            if(data.group!!.name!=null){
                itemView.tv_group_list.text = data.group!!.name
            }
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener {

                itemclick.onClickItem(position, MyConstants.JOIN_GROUP_LIST)
                //    listener.bookSession(position,data)
            }
        }

    }
    }