package com.cts.teamplayer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.WebViewActivity
import com.cts.teamplayer.models.UserListItem
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.adapter_benchmark_list.view.*
import kotlinx.android.synthetic.main.adapter_benchmark_list.view.tv_name_in_benchmark_list
import kotlinx.android.synthetic.main.adapter_maltiple_invite.view.*

class MaltipleInviteAdapter (val mContext: Context, var data : ArrayList<String>, var itemclick: ItemClickListner) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MaltipleInviteAdapter.MyHolderView>(){
    var answerFilterList : ArrayList<UserListItem> = ArrayList()
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var countryFilterList : ArrayList<UserListItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_maltiple_invite, viewGroup, false)
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

        fun bind(data: String, position: Int) {
            itemView.tv_multiple_invite.text=" "+data.toString()
            itemView.tv_number_of_multiple_invite.text=position.toString()
          //  answerFilterList.add(data)


            //     var  flagurl="http://35.160.227.253/SaharaGo/Flag/"+data.flag
            /* Picasso.with(mContext).load(flagurl).error(R.drawable.notification).into(itemView.iv_country_image)
 */
          /*  itemView.iv_delete_benchmark.setOnClickListener{
                //   itemclick.onClickItem(position, MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE)


                itemclick.onClickItem(position, MyConstants.DELETE_BEANCHMARK_REQUEST_CODE)
                answerFilterList.remove(data)
                //  (mContext as ManageTeamActivity).getBanchmarkList(answerFilterList)
                //    listener.bookSession(position,data)
            }*/
            /*  {"group_id":"4","user_id":"3664","subgroup_id":"5","user_type":"benchmark"}*/

         /*   itemView.iv_view_report.setOnClickListener{
                if(TeamPlayerSharedPrefrence.getInstance(mContext).getRoal("").equals("3")){
                    itemclick.onClickItem(position, MyConstants.GENERATE_BEANCHMARK_VIEW_REPORT)
                }else{
                    val i = Intent(mContext, WebViewActivity::class.java).putExtra("group_id",data.groupId)
                        .putExtra("user_id",data.userId).putExtra("subgroup_id",data.subgroupId).putExtra("user_type",data.userType).putExtra("activity", "report")
                    mContext.startActivity(i)
                }*/


                /*         val i = Intent(this, WebViewActivity::class.java).putExtra(
                             "group_id", intent.getStringExtra(
                                 "group_id"
                             )
                         )
                             .putExtra("user_id", intent.getStringExtra("user_id")).putExtra("subgroup_id", intent.getStringExtra("subgroup_id")
                             ).putExtra("user_type", intent.getStringExtra("user_type")).putExtra("activity", "report")
                         startActivity(i)*/


            }
        }

    }
