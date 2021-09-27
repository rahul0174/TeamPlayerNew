package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.SubGroupList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.group_list.view.*


class CustomParticipantTeamList(val mContext: Context, var data : java.util.ArrayList<SubGroupList>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CustomParticipantTeamList.MyHolderView>(){
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

        fun bind(data: SubGroupList, position: Int) {
            itemView.tv_group_list.text=data.name
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
             //   itemclick.onClickItem(position, MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE)
                itemclick.onClickItem(position, MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}