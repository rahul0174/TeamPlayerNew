package com.cts.teamplayer.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.GroupManageActivity
import com.cts.teamplayer.activities.MainActivity
import com.cts.teamplayer.activities.QuestionnaireCalculatorActivity
import com.cts.teamplayer.adapters.CityListAdapter
import com.cts.teamplayer.adapters.GroupJoinListAdapter
import com.cts.teamplayer.adapters.GroupListAdapter
import com.cts.teamplayer.adapters.PendingGroupListAdapter
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.dialog_country.recycler_country_list
import kotlinx.android.synthetic.main.dialog_open_questionnaire.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_demo_group.view.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class InviteGroupListFragment: Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    var groupList: java.util.ArrayList<GroupListItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_invite_group_list, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        finid()
        return v
    }

    private fun finid() {
      //  v.edit_invite_participat_email_im.setOnClickListener(this)
      //  groupJoinList()
      //  pendinggroupList()
    }

    override fun onStart() {
        super.onStart()
        groupList()
    }

    override fun onClick(v: View?) {

    }

    override fun onResume() {
        super.onResume()
       /* groupList()*/

    }

    fun setGroupList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
       v.recycler_invite_group_list.layoutManager = manager
        val   groupListAdapter =  GroupListAdapter(activity!!, groupList,this)
        v.recycler_invite_group_list.adapter = groupListAdapter
    }

    fun setPendingGroupList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
       v.recycler_pending_group_list.layoutManager = manager
        val   groupListAdapter =  PendingGroupListAdapter(activity!!, pendinggroupList,this)
        v.recycler_pending_group_list.adapter = groupListAdapter
    }

    private fun groupList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<GroupListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.groupListParameter(mpref!!.getAccessToken(""))
            call!!.enqueue(object : Callback<GroupListResponse> {
                override fun onResponse(
                    call: Call<GroupListResponse>,
                    response: retrofit2.Response<GroupListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            groupList = response.body()!!.data as ArrayList<GroupListItem>?

                            setGroupList()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<GroupListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    var pendinggroupList: java.util.ArrayList<PendingJoinGroupDataItem>? = null
    private fun pendinggroupList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<PendingJoinGroupResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.penddingListParameter(mpref!!.getAccessToken(""))
            call!!.enqueue(object : Callback<PendingJoinGroupResponse> {
                override fun onResponse(
                    call: Call<PendingJoinGroupResponse>,
                    response: retrofit2.Response<PendingJoinGroupResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {

                            pendinggroupList = response.body()!!.data!! as ArrayList<PendingJoinGroupDataItem>?


                            setPendingGroupList()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<PendingJoinGroupResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    var dialog:Dialog?=null
    private fun showDialogTeamList() {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_open_questionnaire)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(this.resources.getColor(R.color.full_transparent)))

        dialog!!.window!!.setGravity(Gravity.CENTER)
        val rl_cancel = dialog!!.findViewById(R.id.rl_cancel) as RelativeLayout
        val tv_start_questionnaire = dialog!!.findViewById(R.id.tv_start_questionnaire) as TextView
        val recycler_join_group_list = dialog!!.findViewById(R.id.recycler_join_group_list) as RecyclerView
        val iv_click_group_join_cancel = dialog!!.findViewById(R.id.iv_click_group_join_cancel) as ImageView
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        dialog!!.recycler_join_group_list.layoutManager = manager
        val   groupListAdapter =  GroupJoinListAdapter(activity!!, groupJoinDataItem,this)
        dialog!!.recycler_join_group_list.adapter = groupListAdapter
        dialog!!.show()
        rl_cancel.setOnClickListener {
            dialog!!.dismiss()
        }
        iv_click_group_join_cancel.setOnClickListener {
            dialog!!.dismiss()
        }



    }


    var groupJoinDataItem: java.util.ArrayList<GroupJoinDataItem>? = null
    private fun groupJoinList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<GroupJoinResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getgroupJoinListParameter(mpref!!.getAccessToken("").toString())
            call!!.enqueue(object : Callback<GroupJoinResponse> {
                override fun onResponse(
                    call: Call<GroupJoinResponse>,
                    response: retrofit2.Response<GroupJoinResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            groupJoinDataItem =
                                response.body()!!.data as ArrayList<GroupJoinDataItem>?
                            if(groupJoinDataItem!!.size>0){
                                showDialogTeamList()
                            }

                            //   showCounterTimer()


                            /*for (i in 1..questionList!!.size) {
                                answerList =
                                    response.body()!!.data!!.questions!!.get(i)!!.answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)

                            }*/


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<GroupJoinResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onClickItem(position: Int, requestcode: Int) {

        if(requestcode== MyConstants.INVITE_GROUP_LIST){
            startActivity(Intent(activity!!, GroupManageActivity::class.java).putExtra("GROUP_ID",groupList!!.get(position).id))

        }
        if(requestcode== MyConstants.GROUP_JOIN_REQUEST_CODE){
            val i = Intent(activity, QuestionnaireCalculatorActivity::class.java)
            startActivity(i)

        }
        if(requestcode== MyConstants.JOIN_GROUP_LIST){
         /*   {"id":2,"name":"Test Demo Group","group_id":73,"code":"1234","max_size":"1","test":"2"}
         */   /*{"id":2,"name":"Test Demo Group","code":"1234","max_size":"1","test":"2"}*/
            var sendReminderRequestRequest: JsonObject = JsonObject()
            sendReminderRequestRequest!!.addProperty("id", pendinggroupList!!.get(position).id)
            sendReminderRequestRequest!!.addProperty("name", pendinggroupList!!.get(position).group!!.name)
            sendReminderRequestRequest!!.addProperty("group_id", pendinggroupList!!.get(position).groupId)
            sendReminderRequestRequest!!.addProperty("code", pendinggroupList!!.get(position).group!!.code)
            sendReminderRequestRequest!!.addProperty("max_size", pendinggroupList!!.get(position).group!!.maxSize)
            sendReminderRequestRequest!!.addProperty("test",pendinggroupList!!.get(position).group!!.test )

            joinGroupApi(sendReminderRequestRequest)


        }
    }
    private fun joinGroupApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.joinGroupRequest(
                TeamPlayerSharedPrefrence.getInstance(activity!!).getAccessToken(
                    ""
                ), jsonObject
            )
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    // Log.e("log",response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            Log.d("response", response.body()!!.toString())
                            val jsonObject = JSONObject(response.body().toString())
                            Toast.makeText(
                                activity!!,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            groupList()
                            dialog!!.dismiss()


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

}
