package com.cts.teamplayer.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
/*import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult*/
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.BenchmarkListAdapter
import com.cts.teamplayer.adapters.GroupListInManageTeamAdapter
import com.cts.teamplayer.adapters.ParticipantListAdapter
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.MyConstants.COMPATIBILTY_REPORT_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.DELETE_BEANCHMARK_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.DELETE_PARTICIPANT_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.GENERATE_BEANCHMARK_VIEW_REPORT
import com.cts.teamplayer.util.MyConstants.GENERATE_PARTICIPANT_VIEW_REPORT
import com.cts.teamplayer.util.MyConstants.SELECT_BEANCHMARK_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.SELECT_PARTICIPANT_REQUEST_CODE
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_manage_team.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.ArrayList

class ManageTeamActivity : AppCompatActivity() , View.OnClickListener,ItemClickListner {
    private var mpref: TeamPlayerSharedPrefrence? = null
    private var billingClient: BillingClient? = null
    var position: Int? = null
    var which_report_generate: String? = null
    var position_report: Int? = null
    var userListItem: java.util.ArrayList<UserListItem>? = null
    var userListItemBanchMark: java.util.ArrayList<UserListItem>? = ArrayList()
    var userListItemParticipant: java.util.ArrayList<UserListItem>? = ArrayList()
    var userListNew: java.util.ArrayList<UserListItem>? = ArrayList()
    var REQUEST_CODE = 11

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
        appSubscriptionList()
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

        if(userListItemBanchMark!!.size>0){
            img_bench_mark_group_list_empty.visibility=View.GONE
            recycler_benchmarklist.visibility=View.VISIBLE
            var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler_benchmarklist.layoutManager = manager
            val   groupListAdapter =  BenchmarkListAdapter(this, userListItemBanchMark, this)
            recycler_benchmarklist.adapter = groupListAdapter
            groupListAdapter.notifyDataSetChanged()
        }else{
            img_bench_mark_group_list_empty.visibility=View.VISIBLE
            recycler_benchmarklist.visibility=View.GONE
        }

    }
    private fun setParticipantListInRecycler(){
        if(userListItemParticipant!!.size>0){
            img_participant_group_list_empty.visibility=View.GONE
            recycler_participantlist.visibility=View.VISIBLE
            tv_parti_title.text="There are "+userListItemParticipant!!.size.toString()+" participants in this questionnaire group.Click a participant's name to view their questionnaire results."

            var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler_participantlist.layoutManager = manager
            val   groupListAdapter =  ParticipantListAdapter(this, userListItemParticipant, this)
            recycler_participantlist.adapter = groupListAdapter
            groupListAdapter.notifyDataSetChanged()
        }else{
            tv_parti_title.text="There are "+0+" participants in this questionnaire group.Click a participant's name to view their questionnaire results."

            img_participant_group_list_empty.visibility=View.VISIBLE
            recycler_participantlist.visibility=View.GONE
        }

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
        if(requestcode==GENERATE_BEANCHMARK_VIEW_REPORT){
            which_report_generate="beanchmark"
            position_report=position1
            setUpBillingClient()
           // getbraintreeTokenApi()
            /*     val i = Intent(this@ManageTeamActivity, WebViewActivity::class.java).putExtra("group_id",userListItemBanchMark!!.get(position1).groupId)
                     .putExtra("user_id",userListItemBanchMark!!.get(position1).userId).putExtra("subgroup_id",userListItemBanchMark!!.get(position1).subgroupId).putExtra("user_type",userListItemBanchMark!!.get(position1).userType).putExtra("activity", "report")
                 this@ManageTeamActivity.startActivity(i)*/

        }
        if(requestcode== GENERATE_PARTICIPANT_VIEW_REPORT){
            which_report_generate="participant"
            position_report=position1
            setUpBillingClient()
          //  getbraintreeTokenApi()
            /*   val i = Intent(this@ManageTeamActivity, WebViewActivity::class.java).putExtra("group_id",userListItemBanchMark!!.get(position1).groupId)
                   .putExtra("user_id",userListItemBanchMark!!.get(position1).userId).putExtra("subgroup_id",userListItemBanchMark!!.get(position1).subgroupId).putExtra("user_type",userListItemBanchMark!!.get(position1).userType).putExtra("activity", "report")
               this@ManageTeamActivity.startActivity(i)*/

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


    var brintreeToken=""
    var orderId=""
    private fun getbraintreeTokenApi() {
        //   Log.e("json",jsonObject.toString())
        if (CheckNetworkConnection.isConnection1(this@ManageTeamActivity, true)) {
            val progress = ProgressDialog(this@ManageTeamActivity)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this@ManageTeamActivity)
            var call: Call<JsonObject>?
            call = apiInterface!!.getclienttokenParameter(
                TeamPlayerSharedPrefrence.getInstance(
                    this@ManageTeamActivity
                ).getAccessToken("")
            )
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    Log.e("response111", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            val jsonObject = JSONObject(response.body().toString())
                            /* "{
                             ""orderId"" : ""ORD12345"",
                             ""brainTreeToken"" : ""token123"",
                             ""success"" : true
                         }"*/

                            //    orderId = jsonObject.optString("orderId").toString()
                            brintreeToken = jsonObject.optString("token").toString()

                           /* val dropInRequest =
                                DropInRequest().clientToken(brintreeToken).collectDeviceData(
                                    true
                                )*/
                           /* startActivityForResult(
                                dropInRequest.getIntent(this@ManageTeamActivity),
                                REQUEST_CODE
                            )*/

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }


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
                            Toast.makeText(this@ManageTeamActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
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
                this@ManageTeamActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
             //   val result: DropInResult? = data!!.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce = "result!!.paymentMethodNonce"
                val stringNonce = "nonce!!.nonce"
                if(which_report_generate.equals("beanchmark")){

                    val amount1=   BigDecimal(userListItemParticipant!!.size+1)
                    val amount2=   BigDecimal(ppclist!!.get(0).amount)
                    val total_amount:BigDecimal=amount1.multiply(amount2)
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("transaction_id", stringNonce)
                    jsonObject.addProperty("id", userListItemBanchMark!!.get(position_report!!).id)
                    jsonObject.addProperty("amount", total_amount)
                    jsonObject.addProperty(
                        "sub_group_id",
                        userListItemBanchMark!!.get(position_report!!).subgroupId
                    )
                    jsonObject.addProperty(
                        "data",
                        ""
                    )

                    Log.e("json", jsonObject.toString())
                    updateAppSubscriptionPayment(jsonObject)
                }else{


                    val amount1=   BigDecimal(userListItemBanchMark!!.size+1)
                    val amount2=   BigDecimal(ppclist!!.get(0).amount)
                    val total_amount:BigDecimal=amount1.multiply(amount2)
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("transaction_id", stringNonce)
                    jsonObject.addProperty("id", userListItemParticipant!!.get(position_report!!).id)
                    jsonObject.addProperty("amount", total_amount)
                    jsonObject.addProperty(
                        "sub_group_id",
                        userListItemParticipant!!.get(position_report!!).subgroupId
                    )
                    jsonObject.addProperty(
                        "data",
                        ""
                    )

                    Log.e("json", jsonObject.toString())
                    updateAppSubscriptionPayment(jsonObject)
                }
                /*  "{
                  ""orderId"" : ""ORD12345"",
                  ""amount"" : 450,
                  ""nonceFromTheClient"" : ""nonce123""
              }"*/
                /* {"nonce":"tokencc_bh_q67qb6_ntvz2q_qs9yxp_qnzm3y_k84","chargeAmount":0.99}*/
                //   val x=v.edit_number_of_participants.text!!.trim()
// Assign two BigDecimal objects

                // Assign two BigDecimal objects
                /*          val b1 = BigDecimal(v.edit_number_of_participants.text!!.trim().toString())
                           val b2 = BigDecimal(PerQuestionPriceResponseDataItem!!.get(0).amount)*/

                // Multiply b1 with b2 and assign result to b3

                // Multiply b1 with b2 and assign result to b3
                //    val b3: BigDecimal = b1.multiply(b2)
                /*     val b3: BigDecimal = b1.multiply(b2)
                     val y = 13
                     val z = x * y*/


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.e("mylog", "user canceled")
            } else {
                // handle errors here, an exception may be available in
             //   val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR)

                //Log.e("error", error.toString())

            }
        }

    }

    var ppclist: java.util.ArrayList<PPCList>? = null
    private fun appSubscriptionList() {
        if (CheckNetworkConnection.isConnection1(this@ManageTeamActivity, true)) {
            val progress = ProgressDialog(this@ManageTeamActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this@ManageTeamActivity)
            var call: Call<PPCResponse>? = null//apiInterface.profileImage(body,token);


            call = apiInterface!!.getPPCAmount(mpref!!.getAccessToken(""))



            call!!.enqueue(object : Callback<PPCResponse> {
                override fun onResponse(
                    call: Call<PPCResponse>,
                    response: retrofit2.Response<PPCResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            ppclist = response.body()!!.data as ArrayList<PPCList>?





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
                            Toast.makeText(this@ManageTeamActivity, message, Toast.LENGTH_LONG).show()
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

                override fun onFailure(call: Call<PPCResponse>, t: Throwable) {
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
                this@ManageTeamActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun updateAppSubscriptionPayment(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this@ManageTeamActivity, true)) {
            val progress = ProgressDialog(this@ManageTeamActivity)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this@ManageTeamActivity)
            var call: Call<JsonObject>?
            call = apiInterface!!.getUpdateupdatePPCPaymentt(
                TeamPlayerSharedPrefrence.getInstance(this@ManageTeamActivity).getAccessToken(
                    ""
                ), jsonObject
            )
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        var jsonObject = JSONObject(response.body()!!.toString())
                        try {
                            Log.d("response", response.body()!!.toString())
                            Toast.makeText(
                                this@ManageTeamActivity,
                                "Payment done sucessfully",
                                Toast.LENGTH_LONG
                            ).show()
                            if(which_report_generate.equals("beanchmark")){
                                val i = Intent(this@ManageTeamActivity, WebViewActivity::class.java).putExtra("group_id",userListItemBanchMark!!.get(position_report!!).groupId)
                                    .putExtra("user_id",userListItemBanchMark!!.get(position_report!!).userId).putExtra("subgroup_id",userListItemBanchMark!!.get(position_report!!).subgroupId).putExtra("user_type",userListItemBanchMark!!.get(position_report!!).userType).putExtra("activity", "report")
                                this@ManageTeamActivity.startActivity(i)
                            }else{
                                val i = Intent(this@ManageTeamActivity, WebViewActivity::class.java).putExtra("group_id",userListItemParticipant!!.get(position_report!!).groupId)
                                    .putExtra("user_id",userListItemParticipant!!.get(position_report!!).userId).putExtra("subgroup_id",userListItemParticipant!!.get(position_report!!).subgroupId).putExtra("user_type",userListItemParticipant!!.get(position_report!!).userType).putExtra("activity", "report")
                                this@ManageTeamActivity.startActivity(i)
                            }

                            //    Toast.makeText(this@ManageTeamActivity, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                            /*  var cartTable = dbController!!.getCartInfo("")
                            cartTable.clear()*/

                            /*  startActivity(
                                Intent(this@ManageTeamActivity, OrderDoneActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK))*/

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

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
                            Toast.makeText(this@ManageTeamActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
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
                this@ManageTeamActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun setUpBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        startConnection()

    }
    private fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    Log.v("TAG_INAPP","Setup Billing Done")
                    // The BillingClient is ready. You can query purchases here.

                    queryAvaliableProducts("userplan")


                }
            }
            override fun onBillingServiceDisconnected() {
                Log.v("TAG_INAPP","Billing client Disconnected")

            }
        })
    }
    private fun queryAvaliableProducts(id:String) {
        val skuList = ArrayList<String>()
        skuList.add(id)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient?.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                for (skuDetails in skuDetailsList) {
                    Log.v("TAG_INAPP","skuDetailsList : ${skuDetailsList}")
                    skuDetails?.let {
                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()
                        billingClient?.launchBillingFlow(this, billingFlowParams)?.responseCode.toString()

                    }
                }
            }
        }
    }

    private val purchaseUpdateListener =  PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("transaction_id", "")
                jsonObject.addProperty("id", userListItemBanchMark!!.get(position_report!!).id)
                jsonObject.addProperty("amount", "0.99")
                jsonObject.addProperty(
                    "sub_group_id",
                    userListItemBanchMark!!.get(position_report!!).subgroupId
                )
                jsonObject.addProperty(
                    "data",
                    ""
                )

                Log.e("json", jsonObject.toString())
                updateAppSubscriptionPayment(jsonObject)

            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }



}
