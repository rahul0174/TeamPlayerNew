package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.InviteeListDataItem
import com.cts.teamplayer.models.SurveyParticipantsItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_invitee_list.view.*
import kotlinx.android.synthetic.main.adapter_participant_on_group_list.view.*

class InviteeListAdapter (val mContext: Context, var data : java.util.ArrayList<InviteeListDataItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<InviteeListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<InviteeListDataItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_invitee_list, viewGroup, false)
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

        fun bind(data: InviteeListDataItem, position: Int) {
            itemView.tv_email_invitee.text=data.email
            itemView.tv_date_invitee.text=data.onDate
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.tv_resend_invitee.setOnClickListener{
                /* Toast.makeText(mContext, "In progress", Toast.LENGTH_LONG)
                     .show()*/
                    itemclick.onClickItem(position, MyConstants.RESEND_INVITEE_EMAIL)


            }
          itemView.img_delete_invitee.setOnClickListener{

              itemclick.onClickItem(position, MyConstants.DELETE_INVITEE_EMAIL)


            }

        }

    }
    fun filterList(filterdNames: java.util.ArrayList<InviteeListDataItem>): java.util.ArrayList<InviteeListDataItem> {
        this.data = filterdNames
        notifyDataSetChanged()
        return data as java.util.ArrayList<InviteeListDataItem>
    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}