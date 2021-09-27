package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.SubGroupList
import com.cts.teamplayer.models.UserListItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_group_list_in_team_manage.view.*
import kotlinx.android.synthetic.main.group_list.view.*

class GroupListInManageTeamAdapter (val mContext: Context, var data : java.util.ArrayList<UserListItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<GroupListInManageTeamAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<UserListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_group_list_in_team_manage, viewGroup, false)
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

        fun bind(data: UserListItem, position: Int) {

            if(data.userType.equals("participant")){
                itemView.img_select_participant.setBackgroundResource(R.drawable.radio_check);
                itemView.img_select_beanchmark.setBackgroundResource(R.drawable.radio_uncheck);
            }else{
                itemView.img_select_beanchmark.setBackgroundResource(R.drawable.radio_check);
                itemView.img_select_participant.setBackgroundResource(R.drawable.radio_uncheck);

            }
            itemView.tv_user_name_in_manage_team.text="Name: "+data.userName




            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.img_select_beanchmark.setOnClickListener{
                //   itemclick.onClickItem(position, MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE)
                itemclick.onClickItem(position, MyConstants.SELECT_BEANCHMARK_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }
            itemView.img_select_participant.setOnClickListener{
                //   itemclick.onClickItem(position, MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE)
                itemclick.onClickItem(position, MyConstants.SELECT_PARTICIPANT_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }
        }

    }




}