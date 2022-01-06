package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.AppQuestionPurchase
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_history.view.*
import kotlinx.android.synthetic.main.adapter_invitee_list.view.*
import kotlinx.android.synthetic.main.adapter_invitee_list.view.tv_email_invitee

class HistoryAdapter(val mContext: Context, var data : java.util.ArrayList<AppQuestionPurchase>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<HistoryAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<AppQuestionPurchase>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_history, viewGroup, false)
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

        fun bind(data: AppQuestionPurchase, position: Int) {
            itemView.tv_test.text=data.test
            itemView.tv_transaction_type.text=data.noOfParticipant
            itemView.tv_date_his.text=data.onDate
            itemView.tv_amount_his.text=data.amount
            itemView.tv_history_title.text=data.plantitle
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
        /*    itemView.tv_resend_invitee.setOnClickListener{
                *//* Toast.makeText(mContext, "In progress", Toast.LENGTH_LONG)
                     .show()*//*
                itemclick.onClickItem(position, MyConstants.RESEND_INVITEE_EMAIL)


            }
            itemView.img_delete_invitee.setOnClickListener{

                itemclick.onClickItem(position, MyConstants.DELETE_INVITEE_EMAIL)


            }*/

        }

    }
}