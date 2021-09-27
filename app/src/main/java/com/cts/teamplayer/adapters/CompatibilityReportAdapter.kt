package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CompatibilitySurveyUserListItem
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.network.ItemClickListner
import kotlinx.android.synthetic.main.adapter_compatibility_report.view.*

class CompatibilityReportAdapter(
    val mContext: Context,
    var data: java.util.ArrayList<CompatibilitySurveyUserListItem>?,
    var itemclick: ItemClickListner,var nameid: String,
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CompatibilityReportAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<CompatibilitySurveyUserListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_compatibility_report, viewGroup, false)
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

        fun bind(data: CompatibilitySurveyUserListItem, position: Int) {

            if (nameid.equals("name")){
                itemView.tv_name_id.text=data.name

            }else{
                itemView.tv_name_id.text=data.id

            }


            var n = data.score
            itemView. progressbar.max=data.maxScore!!
            itemView. progressbar.progress= data.score!!




            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
           /* itemView.setOnClickListener{
                *//*         Toast.makeText(
                             mContext,
                             "In Progress",
                             Toast.LENGTH_LONG
                         )
                             .show()*//*
                itemclick.onClickItem(position, MyConstants.MANAGE_TEAM_REQUEST_CODE)
                //    listener.bookSession(position,data)
            }*/
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int, data: CountryList)
    }



}