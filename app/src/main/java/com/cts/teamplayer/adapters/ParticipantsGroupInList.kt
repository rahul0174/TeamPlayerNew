package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.GroupListItem
import com.cts.teamplayer.models.SurveyGroup
import com.cts.teamplayer.models.SurveyParticipantsItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_participant_on_group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.*
import kotlinx.android.synthetic.main.group_list.view.tv_group_list

class ParticipantsGroupInList (val mContext: Context, var data : java.util.ArrayList<SurveyParticipantsItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ParticipantsGroupInList.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<SurveyParticipantsItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_participant_on_group_list, viewGroup, false)
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

        fun bind(data: SurveyParticipantsItem, position: Int) {
            itemView.tv_participants_group_name.text=data.userName
            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
            itemView.setOnClickListener{
               /* Toast.makeText(mContext, "In progress", Toast.LENGTH_LONG)
                    .show()*/
                if(itemView.tv_click_add_to_team.text.toString().equals("Add To Team")){
                    itemclick.onClickItem(position, MyConstants.ADD_TO_TEAM)
                }else{
                    itemclick.onClickItem(position, MyConstants.SEND_REMINDER_REQUEST_CODE)
                /*     Toast.makeText(mContext, "Send Remainder", Toast.LENGTH_LONG)
                    .show()*/
                }


            }
            if(data.surveyProgress.equals("1")){
                itemView.tv_survey_progress.text="Complete"
                itemView.tv_click_add_to_team.text="Add To Team"
            }else{
                itemView.tv_survey_progress.text="Not Complete"
                itemView.tv_click_add_to_team.text="Send Remainder"
            }
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int,data: CountryList)
    }



}