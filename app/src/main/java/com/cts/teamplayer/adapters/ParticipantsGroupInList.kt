package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.SurveyParticipantsItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_participant_on_group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import java.util.*

class ParticipantsGroupInList(
    val mContext: Context,
    var data: java.util.ArrayList<SurveyParticipantsItem>?,
    var itemclick: ItemClickListner
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ParticipantsGroupInList.MyHolderView>(){
     var countryFilterList : ArrayList<SurveyParticipantsItem>? = null
    var countryFilterList1 : ArrayList<SurveyParticipantsItem>? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_participant_on_group_list, viewGroup, false)
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

        fun bind(data: SurveyParticipantsItem, position: Int) {
            itemView.tv_participants_group_name.text="Name: "+data.userName
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
               /* Toast.makeText(mContext, "In progress", Toast.LENGTH_LONG)
                    .show()*/
                if(itemView.tv_click_add_to_team.text.toString().equals("Add to Team")){
                    itemclick.onClickItem(position, MyConstants.ADD_TO_TEAM)
                }else{
                    itemclick.onClickItem(position, MyConstants.SEND_REMINDER_REQUEST_CODE)
                /*     Toast.makeText(mContext, "Send Remainder", Toast.LENGTH_LONG)
                    .show()*/
                }


            }
            if(data.surveyProgress.equals("1")){
                itemView.tv_survey_progress.text="Status: "+"Complete"
                itemView.tv_click_add_to_team.text="Add to Team"
            }else{
                itemView.tv_survey_progress.text="Status: "+"Not Complete"
                itemView.tv_click_add_to_team.text="Send Remainder"
            } /*if(data.surveyProgress.equals("true")){
                itemView.tv_survey_progress.text="Status: "+"Complete"
                itemView.tv_click_add_to_team.text="Add to Team"
            }else{
                itemView.tv_survey_progress.text="Status: "+"Not Complete"
                itemView.tv_click_add_to_team.text="Send Remainder"
            }*/
        }

    }


    /*
   View Holders
   _________________________________________________________________________________________________

   private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<CountryList> = ArrayList()

        //looping through existing elements
        for (s in this!!.countryList!!) {
            //if the existing elements contains the search input
            if (s!!.countryName.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = countryListAdapter!!.filterList(filterdNames)
    }

    */


/*    fun filter(charText: String) {
        countryFilterList = data!! as ArrayList<SurveyParticipantsItem>?
      //  this.countryFilterList!!.addAll(data!!)
     //   this.data!!.addAll(countryFilterList!!)
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
       // data!!.clear()
        if (charText.length == 0) {
            data!!.addAll(countryFilterList!!)
        } else {

            for (item in countryFilterList!!) {
              //  data!!.clear()
                if (item.userName!!.toLowerCase(Locale.getDefault()).contains(charText)) {

                    data!!.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }*/

    interface TextBookNow{
        fun bookSession(position: Int, data: CountryList)
    }

    fun filterList(filterdNames: ArrayList<SurveyParticipantsItem>): ArrayList<SurveyParticipantsItem> {
        this.data = filterdNames
        notifyDataSetChanged()
        return data as ArrayList<SurveyParticipantsItem>
    }



}