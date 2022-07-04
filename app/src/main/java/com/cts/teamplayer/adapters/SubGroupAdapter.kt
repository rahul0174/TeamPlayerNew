package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.SubGroupList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_sub_group.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.tv_group_list

class SubGroupAdapter (val mContext: Context, var data : java.util.ArrayList<SubGroupList>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SubGroupAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<GroupListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_sub_group, viewGroup, false)
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
            itemView.tv_team_name_new.text="Team Name: "+data.name
             if(data.userList!!.size>0){
                itemView.tv_no_of_participat_new.text="Participants: "+data.userList.size
            }else{
                itemView.tv_no_of_participat_new.text="No. of Participants: "+"0"
            }
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
       /*         Toast.makeText(
                    mContext,
                    "In Progress",
                    Toast.LENGTH_LONG
                )
                    .show()*/
                itemclick.onClickItem(position, MyConstants.MANAGE_TEAM_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}