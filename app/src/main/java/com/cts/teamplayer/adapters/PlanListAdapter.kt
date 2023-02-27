package com.cts.teamplayer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.PlanList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.MyConstants.NEW_PAYPAL_CLICK_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.PAYPAL_CLICK_REQUEST_CODE
import kotlinx.android.synthetic.main.adapter_plan_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.tv_group_list

class PlanListAdapter  (val mContext: Context, var data : java.util.ArrayList<PlanList>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<PlanListAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<PlanList>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_plan_list, viewGroup, false)
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

        fun bind(data: PlanList, position: Int) {
            Log.e("planList",data.id.toString())
            if(data.amount.equals("50.00")){
                itemView.tv_plan_amount.text="New User Plan: £"+data.amount+" or CV"

            }else{
                itemView.tv_plan_amount.text="Individual Sign up: £"+data.amount+" or CV"

            }
              //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.img_paypal_on_clik.setOnClickListener{
                if(data.amount.equals("50.00")){
                    itemclick.onClickItem(position,NEW_PAYPAL_CLICK_REQUEST_CODE)
                      }else{
                    itemclick.onClickItem(position,PAYPAL_CLICK_REQUEST_CODE)

                }
                      //    listener.bookSession(position,data)
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}