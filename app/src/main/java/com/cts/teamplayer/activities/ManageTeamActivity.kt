package com.cts.teamplayer.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.BenchmarkListAdapter
import com.cts.teamplayer.adapters.GroupListInManageTeamAdapter
import com.cts.teamplayer.adapters.ParticipantListAdapter
import com.cts.teamplayer.models.SubGroupResponse
import com.cts.teamplayer.models.UserListItem
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.MyConstants.COMPATIBILTY_REPORT_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.DELETE_BEANCHMARK_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.DELETE_PARTICIPANT_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.SELECT_BEANCHMARK_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.SELECT_PARTICIPANT_REQUEST_CODE
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_manage_team.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class ManageTeamActivity : AppCompatActivity() , View.OnClickListener,ItemClickListner {
    private var mpref: TeamPlayerSharedPrefrence? = null
    var position: Int? = null
    var userListItem: java.util.ArrayList<UserListItem>? = null
    var userListItemBanchMark: java.util.ArrayList<UserListItem>? = ArrayList()
    var userListItemParticipant: java.util.ArrayList<UserListItem>? = ArrayList()
    var userListNew: java.util.ArrayList<UserListItem>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_team)
        mpref = TeamPlayerSharedPrefrence.getInstance(this)
         position= intent .getIntExtra( "POSITION",0)
        findId()
    }

    private fun findId() {
        SubGroupListApi()
        img_back_on_manage_team  .setOnClickListener{
            finish()
             }
        img_back_on_manage_team
    }
    private fun SubGroupListApi() {

        var subGroupRequest: JsonObject = JsonObject()
        subGroupRequest!!.addProperty("group_id", intent.getStringExtra("GROUP_ID"))
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<SubGroupResponse>? = null//apiInterface.profileImage(body,token);

            call = apiInterface!!.subGroupListParameter(mpref!!.getAccessToken(""), subGroupRequest)
            call!!.enqueue(object : Callback<SubGroupResponse> {
                override fun onResponse(
                    call: Call<SubGroupResponse>,
                    response: retrofit2.Response<SubGroupResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {

                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)
                        userListItem = response.body()!!.data!!.get(position!!)!!.userList as ArrayList<UserListItem>?
                        userListNew!!.clear()
                        userListItemParticipant!!.clear()
                        userListItemBanchMark!!.clear()
                        for (j in 0..userListItem!!.size-1){
                            if (userListItem!!.get(j).userType.equals("participant")){
                                getParticipantList(userListItem!!.get(j))

                            }else if (userListItem!!.get(j).userType.equals("benchmark")){
                                getBanchmarkList(userListItem!!.get(j))

                            }else{
                                getGroupList(userListItem!!.get(j))

                            }

                        }

                        setBenchMarkListInRecycler()
                        setParticipantListInRecycler()

                        setGroupListInRecycler()

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@ManageTeamActivity,
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
                            Toast.makeText(this@ManageTeamActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ManageTeamActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SubGroupResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@ManageTeamActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun setGroupListInRecycler(){
        if(userListNew!!.size>0){
            img_group_list_empty.visibility=View.GONE
            recycler_group_list.visibility=View.VISIBLE
            var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler_group_list.layoutManager = manager
            val   groupListAdapter =  GroupListInManageTeamAdapter(this, userListNew, this)
            recycler_group_list.adapter = groupListAdapter
            groupListAdapter.notifyDataSetChanged()
        }else{
            img_group_list_empty.visibility=View.VISIBLE
            recycler_group_list.visibility=View.GONE
        }

    }
    private fun setBenchMarkListInRecycler(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_benchmarklist.layoutManager = manager
        val   groupListAdapter =  BenchmarkListAdapter(this, userListItemBanchMark, this)
        recycler_benchmarklist.adapter = groupListAdapter
        groupListAdapter.notifyDataSetChanged()
    }
    private fun setParticipantListInRecycler(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_participantlist.layoutManager = manager
        val   groupListAdapter =  ParticipantListAdapter(this, userListItemParticipant, this)
        recycler_participantlist.adapter = groupListAdapter
        groupListAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
         /*   R.id.btn_send-> {

            }*/
        }
    }

    override fun onClickItem(position1: Int, requestcode: Int) {
        if(requestcode==SELECT_BEANCHMARK_REQUEST_CODE){
       /*     {"group_id":"4","user_id":"3664","subgroup_id":"5","user_type":"benchmark"}
       */
            var surveyRequest: JsonObject = JsonObject()
          /*  surveyRequest!!.addProperty("group_id", userListItem!!.get(position).groupId)
            surveyRequest!!.addProperty("user_id", userListItem!!.get(position).userId)
          */  surveyRequest!!.addProperty("id", userListNew!!.get(position1).id)
            surveyRequest!!.addProperty("user_type","benchmark" )

            SurveyResulttApi(surveyRequest)
        }


       /* userListItemBanchMark!!.add(userListItem!!.get(position))
        userListItemParticipant!!.remove(userListItem!!.get(position))
*/

        if(requestcode==SELECT_PARTICIPANT_REQUEST_CODE){

            var surveyRequest: JsonObject = JsonObject()
            surveyRequest!!.addProperty("id", userListNew!!.get(position1).id)
            surveyRequest!!.addProperty("user_type","participant" )
            SurveyResulttApi(surveyRequest)

        }
        if(requestcode==DELETE_PARTICIPANT_REQUEST_CODE){

            var surveyRequest: JsonObject = JsonObject()
            surveyRequest!!.addProperty("id", userListItemParticipant!!.get(position1).id)
            surveyRequest!!.addProperty("user_type","" )
            SurveyResulttApi(surveyRequest)

        }
        if(requestcode==DELETE_BEANCHMARK_REQUEST_CODE){

            var surveyRequest: JsonObject = JsonObject()
            surveyRequest!!.addProperty("id", userListItemBanchMark!!.get(position1).id)
            surveyRequest!!.addProperty("user_type","" )
            SurveyResulttApi(surveyRequest)

        }
        if(requestcode==COMPATIBILTY_REPORT_REQUEST_CODE){

            var surveyRequest: JsonObject = JsonObject()
            surveyRequest!!.addProperty("id", userListItemBanchMark!!.get(position1).id)
            surveyRequest!!.addProperty("user_type","" )
            SurveyResulttApi(surveyRequest)

        }

    }
    fun getBanchmarkList(selected: UserListItem) {
        userListItemBanchMark!!.addAll(listOf(selected))

    }

    fun getGroupList(selected: UserListItem) {
        userListNew!!.addAll(listOf(selected))
    }
    fun getParticipantList(selected: UserListItem) {

        userListItemParticipant!!.addAll(listOf(selected))
    }
    private fun SurveyResulttApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);

            call = apiInterface!!.surveyResultParameter(mpref!!.getAccessToken(""), jsonObject)
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {

                        try {
                            Log.d("response", response.body()!!.toString())
                            val jsonObject = JSONObject(response.body().toString())
                            Toast.makeText(
                                this@ManageTeamActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()

                            SubGroupListApi()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@ManageTeamActivity,
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
                            Toast.makeText(this@ManageTeamActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ManageTeamActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@ManageTeamActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

}
