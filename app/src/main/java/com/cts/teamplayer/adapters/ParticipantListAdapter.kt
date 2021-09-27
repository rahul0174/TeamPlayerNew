package com.cts.teamplayer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.CompatibilityReportActivity
import com.cts.teamplayer.activities.ManageTeamActivity
import com.cts.teamplayer.activities.WebViewActivity
import com.cts.teamplayer.models.UserListItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import kotlinx.android.synthetic.main.adapter_benchmark_list.view.*

class ParticipantListAdapter (val mContext: Context, var data : java.util.ArrayList<UserListItem>?, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ParticipantListAdapter.MyHolderView>(){
    var answerFilterList : ArrayList<UserListItem> = ArrayList()
    lateinit var countryFilterList : ArrayList<UserListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_benchmark_list, viewGroup, false)
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
            itemView.tv_name_in_benchmark_list.text="Name: "+data.userName
            answerFilterList.add(data)
            itemView.iv_delete_benchmark.setOnClickListener{


                 itemclick.onClickItem(position, MyConstants.DELETE_PARTICIPANT_REQUEST_CODE)
                answerFilterList.remove(data)
             //   (mContext as ManageTeamActivity).getParticipantList(answerFilterList)
                  }

            itemView.iv_view_report.setOnClickListener{
                val i = Intent(mContext, WebViewActivity::class.java).putExtra("group_id",data.groupId)
                    .putExtra("user_id",data.userId).putExtra("subgroup_id",data.subgroupId).putExtra("user_type",data.userType).putExtra("activity", "report")
                mContext.startActivity(i)
            }
        }

    }




}