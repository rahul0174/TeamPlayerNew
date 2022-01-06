package com.cts.teamplayer.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.AppSubscriptionList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.adapter_subscription_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*

class SubscriptionListAdapter(
    val mContext: Context,
    var data: java.util.ArrayList<AppSubscriptionList>?,
    var itemclick: ItemClickListner
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SubscriptionListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<AppSubscriptionList>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_subscription_list, viewGroup, false)
        return MyHolderView(v)
    }
    override fun onBindViewHolder(myHolderView: MyHolderView, position: Int) {
        myHolderView.bind(data!!.get(position), position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class MyHolderView(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(data: AppSubscriptionList, position: Int) {
            itemView.tv_subscription_title.text=data.title
            itemView.tv_subscription_freq_type.text=data.frequencyType
            itemView.tv_subscription_amount.text=data.amount
            itemView.tv_subscription_duration.text=data.duration



            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
                itemclick.onClickItem(position, MyConstants.APP_SUBSCRIPTION)
                //    listener.bookSession(position,data)
            }
        }

    }
}