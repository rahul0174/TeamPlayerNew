package com.cts.teamplayer.fragments

import android.app.Activity
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
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.GroupManageActivity
import com.cts.teamplayer.activities.MainActivity
import com.cts.teamplayer.activities.QuestionnaireCalculatorActivity
import com.cts.teamplayer.adapters.*
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.dialog_country.recycler_country_list
import kotlinx.android.synthetic.main.dialog_open_questionnaire.*
import kotlinx.android.synthetic.main.fragment_brief_questionnaire.view.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_demo_group.tv_title_header
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
import java.math.BigDecimal
import java.util.ArrayList

class InviteGroupListFragment: Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    var groupList: java.util.ArrayList<GroupListItem>? = null
    var REQUEST_CODE = 11
    var dialog1:Dialog?=null
    lateinit var homeFragment: androidx.fragment.app.Fragment

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
        // pendinggroupList()
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
    fun setSubscriptionList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        v.recycler_subscription_list.layoutManager = manager
        val   groupListAdapter =  SubscriptionListAdapter(activity!!, appSubscriptionList,this)
        v.recycler_subscription_list.adapter = groupListAdapter
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

                            if (groupList!!.get(0).subscription!!.equals("false")){

                                tv_app_plan_list.visibility=View.VISIBLE
                                recycler_subscription_list.visibility=View.VISIBLE
                                recycler_invite_group_list.visibility=View.GONE
                                tv_some_txt.visibility=View.GONE
                                tv_your_match.visibility=View.GONE
                                appSubscriptionList()


                            }else{
                                tv_app_plan_list.visibility=View.GONE
                                recycler_subscription_list.visibility=View.GONE
                                recycler_invite_group_list.visibility=View.VISIBLE
                                tv_some_txt.visibility=View.VISIBLE
                                tv_your_match.visibility=View.VISIBLE
                                setGroupList()
                            }


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

    var appSubscriptionList: java.util.ArrayList<AppSubscriptionList>? = null
    private fun appSubscriptionList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<AppSubscriptionResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getAppSubscription(mpref!!.getAccessToken(""))
            call!!.enqueue(object : Callback<AppSubscriptionResponse> {
                override fun onResponse(
                    call: Call<AppSubscriptionResponse>,
                    response: retrofit2.Response<AppSubscriptionResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            appSubscriptionList = response.body()!!.data as ArrayList<AppSubscriptionList>?
                            setSubscriptionList()



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

                override fun onFailure(call: Call<AppSubscriptionResponse>, t: Throwable) {
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
                            if(pendinggroupList!!.size>=0){
                                v.tv_join_a.visibility=View.VISIBLE
                                setPendingGroupList()
                            }else{
                                v.tv_join_a.visibility=View.GONE
                                //   v.tv_join_a.visibility=View.GONE
                            }


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

    private fun joinGroup(jsonObject: JsonObject){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.joinGroupParameter(mpref!!.getAccessToken(""),jsonObject)
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

                            val token = jsonObject.getJSONObject("data").optString("token")
                            val role = jsonObject.getJSONObject("data").optString("role")
                            mpref!!.setAccessToken(token)
                            mpref!!.setRoal(role)
                            val i = Intent(activity!!, MainActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)

                            //
                            // Log.d("usertype",user_type);
                            /*       mpref!!.setToken(token)
                            mpref!!.setUserType(usertype)
                            mpref!!.setUserId(usertype)
                           */

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
        if(requestcode== MyConstants.APP_SUBSCRIPTION){
            getbraintreeTokenApi()
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
    var brintreeToken=""
    var orderId=""
    private fun getbraintreeTokenApi() {
        //   Log.e("json",jsonObject.toString())
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>?
            call = apiInterface!!.getclienttokenParameter(
                TeamPlayerSharedPrefrence.getInstance(
                    activity!!
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

                            val dropInRequest =
                                DropInRequest().clientToken(brintreeToken).collectDeviceData(
                                    true
                                )
                            startActivityForResult(
                                dropInRequest.getIntent(activity!!),
                                REQUEST_CODE
                            )

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
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result: DropInResult? = data!!.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce = result!!.paymentMethodNonce
                val stringNonce = nonce!!.nonce
                /*  "{
                  ""orderId"" : ""ORD12345"",
                  ""amount"" : 450,
                  ""nonceFromTheClient"" : ""nonce123""
              }"*/
                /* {"nonce":"tokencc_bh_q67qb6_ntvz2q_qs9yxp_qnzm3y_k84","chargeAmount":0.99}*/
                //   val x=v.edit_number_of_participants.text!!.trim()
// Assign two BigDecimal objects

                // Assign two BigDecimal objects
                //   val b1 = BigDecimal(v.edit_number_of_participants.text!!.trim().toString())
                //    val b2 = BigDecimal(PerQuestionPriceResponseDataItem!!.get(0).amount)

                // Multiply b1 with b2 and assign result to b3

                // Multiply b1 with b2 and assign result to b3
                //   val b3: BigDecimal = b1.multiply(b2)
                /*     val b3: BigDecimal = b1.multiply(b2)
                     val y = 13
                     val z = x * y*/
                val jsonObject = JsonObject()
                jsonObject.addProperty("transaction_id", stringNonce)
                jsonObject.addProperty(
                    "id",
                    appSubscriptionList!!.get(0).id
                )

                Log.e("json", jsonObject.toString())
                updateAppSubscriptionPayment(jsonObject)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.e("mylog", "user canceled")
            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR)

                Log.e("error", error.toString())

            }
        }

    }
    private fun authenticatePaymentApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>?
            call = apiInterface!!.authenticatePayment(
                TeamPlayerSharedPrefrence.getInstance(activity!!).getAccessToken(
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
                                activity!!,
                                "Payment done sucessfully",
                                Toast.LENGTH_LONG
                            ).show()
                            //    Toast.makeText(activity!!, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                            /*  var cartTable = dbController!!.getCartInfo("")
                            cartTable.clear()*/

                            /*  startActivity(
                                Intent(activity!!, OrderDoneActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
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
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
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

    private fun updateAppSubscriptionPayment(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>?
            call = apiInterface!!.updateAppSubscriptionPayment(
                TeamPlayerSharedPrefrence.getInstance(activity!!).getAccessToken(
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
                                activity!!,
                                "Payment done sucessfully",
                                Toast.LENGTH_LONG
                            ).show()
                            dialog1 = Dialog(activity!!)
                            dialog1!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog1!!.setCancelable(false)
                            dialog1!!.setContentView(R.layout.dialog_next_step)
                            dialog1!!.setCanceledOnTouchOutside(true)
                            dialog1!!.window!!.setLayout(
                                WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.MATCH_PARENT
                            )
                            dialog1!!.window!!.setBackgroundDrawable(ColorDrawable(activity!!.resources.getColor(R.color.full_transparent)))

                            dialog1!!.window!!.setGravity(Gravity.CENTER)

                            val rl_no_next_step = dialog1!!.findViewById(R.id.rl_no_next_step) as RelativeLayout
                            val rl_ok_next_step = dialog1!!.findViewById(R.id.rl_ok_next_step) as RelativeLayout

                            dialog1!!.show()
                            rl_no_next_step.setOnClickListener {
                                dialog1!!.dismiss()
                            }
                            rl_ok_next_step.setOnClickListener {
                                dialog1!!.dismiss()

                                activity!!.tv_title_header.text=TeamPlayerSharedPrefrence.getInstance(activity!!).getBusinessName("")
                                homeFragment = AppQuestionnaireFragment()
                                val manager = activity!!.supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.replace(R.id.container, homeFragment)
                                // transaction.addToBackStack(null);
                                transaction.commit()
                            }

                            groupList()
                            //    Toast.makeText(activity!!, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                            /*  var cartTable = dbController!!.getCartInfo("")
                            cartTable.clear()*/

                            /*  startActivity(
                                Intent(activity!!, OrderDoneActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
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
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
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
