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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.MainActivity
import com.cts.teamplayer.activities.QuestionnaireCalculatorActivity
import com.cts.teamplayer.adapters.GroupJoinListAdapter
import com.cts.teamplayer.adapters.PendingGroupListAdapter
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.dialog_join_group.*
import kotlinx.android.synthetic.main.dialog_open_questionnaire.*
import kotlinx.android.synthetic.main.fragment_app_questionnaire.view.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class AppQuestionnaireFragment : Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var groupJoinDataItem: java.util.ArrayList<GroupJoinDataItem>? = null
    var dialog:Dialog?=null
    var dialog1:Dialog?=null
    var view1: View?=null
    lateinit var homeFragment: androidx.fragment.app.Fragment

    var p:Int?=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_app_questionnaire, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
         view1 = activity!!.findViewById(R.id.container)
        groupJoinList()
        pendinggroupList()
        return v
    }

    override fun onClick(v: View?) {
    }
    private fun  setRecyclerAppQuestionnaireAdapter(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        v.recycler_app_questionnaire.layoutManager = manager
        val   groupListAdapter =  GroupJoinListAdapter(activity!!, groupJoinDataItem, this)
        v.recycler_app_questionnaire.adapter = groupListAdapter
    }

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
                            if (groupJoinDataItem!!.size > 0) {
                                setRecyclerAppQuestionnaireAdapter()
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
    var pendinggroupList: java.util.ArrayList<PendingJoinGroupDataItem>? = null
    var grouplist: java.util.ArrayList<Group>? = null
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

                            pendinggroupList =
                                response.body()!!.data!! as ArrayList<PendingJoinGroupDataItem>?
                            grouplist = response.body()!!.data!! as ArrayList<Group>?
                            if (pendinggroupList!!.size > 0) {
                                v.tv_pending_invitation_new.visibility = View.VISIBLE
                                v.recycler_pending_invite_group_list.visibility = View.VISIBLE
                                setPendingGroupList()
                            } else {
                                v.tv_pending_invitation_new.visibility = View.GONE
                                v.recycler_pending_invite_group_list.visibility = View.GONE
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


    fun setPendingGroupList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        v.recycler_pending_invite_group_list.layoutManager = manager
        val   groupListAdapter =  PendingGroupListAdapter(activity!!, pendinggroupList, this)
        v.recycler_pending_invite_group_list.adapter = groupListAdapter
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode== MyConstants.GROUP_JOIN_REQUEST_CODE){
            if(groupJoinDataItem!!.get(position).maxSize.equals("0")){
                dialog1 = Dialog(activity!!)
                dialog1!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog1!!.setCancelable(false)
                dialog1!!.setContentView(R.layout.dialog_no_credit)
                dialog1!!.setCanceledOnTouchOutside(true)
                dialog1!!.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                dialog1!!.window!!.setBackgroundDrawable(ColorDrawable(this.resources.getColor(R.color.full_transparent)))

                dialog1!!.window!!.setGravity(Gravity.CENTER)

                val rl_yes_credit = dialog1!!.findViewById(R.id.rl_yes_credit) as RelativeLayout
                val rl_no_credit = dialog1!!.findViewById(R.id.rl_no_credit) as RelativeLayout

                dialog1!!.show()
                rl_no_credit.setOnClickListener {
                    dialog1!!.dismiss()
                }
                rl_yes_credit.setOnClickListener {
                    mpref!!.setEmail(groupJoinDataItem!!.get(position).id.toString())
                    dialog1!!.dismiss()
                    activity!!.tv_title_header.text=getString(R.string.brief_ques_title)
                    homeFragment = BriefQuestionnaireFragment()
                    val manager = activity!!.supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.container, homeFragment)
                    // transaction.addToBackStack(null);
                    transaction.commit()
                }

            }else{
                mpref!!.setEmail(groupJoinDataItem!!.get(position).id.toString())

                val jsonObject = JsonObject()
                jsonObject.addProperty("test", "2")
                jsonObject.addProperty("group_id", groupJoinDataItem!!.get(position).id.toString())
                setScoreApi(jsonObject)

                activity!!.tv_title_header.text="Invite"
                homeFragment = QuestionnaireCalculator()
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.container, homeFragment)
                // transaction.addToBackStack(null);
                transaction.commit()
           /*     val i = Intent(activity, QuestionnaireCalculatorActivity::class.java)
                mpref!!.setEmail(groupJoinDataItem!!.get(position).id.toString())
                startActivity(i)*/
            }


        }
        if(requestcode== MyConstants.JOIN_GROUP_LIST){

            p=position
            val jsonObject = JsonObject()
            jsonObject.addProperty("test", "2")
            jsonObject.addProperty("group_id", pendinggroupList!!.get(position).groupId)
            setScoreApi(jsonObject)
            /*if(TeamPlayerSharedPrefrence.getInstance(activity!!).getIsQuestionnaireName("").equals("true")){
            }*/
            /*{"id":2,"name":"Test Demo Group","code":"1234","max_size":"1","test":"2"}*/
            dialog = Dialog(activity!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.dialog_join_group)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(this.resources.getColor(R.color.full_transparent)))

            dialog!!.window!!.setGravity(Gravity.CENTER)

            val rl_no_group_join = dialog!!.findViewById(R.id.rl_no_group_join) as RelativeLayout
            val rl_yes_group_join = dialog!!.findViewById(R.id.rl_yes_group_join) as RelativeLayout

            dialog!!.show()
            rl_no_group_join.setOnClickListener {
                dialog!!.dismiss()
            }
            rl_yes_group_join.setOnClickListener {
                var joinRequest: JsonObject = JsonObject()
                joinRequest!!.addProperty("id", pendinggroupList!!.get(p!!).group!!.id)
                joinRequest!!.addProperty("name", pendinggroupList!!.get(p!!).group!!.name)
                joinRequest!!.addProperty("code", pendinggroupList!!.get(p!!).group!!.code)
                joinRequest!!.addProperty("max_size", pendinggroupList!!.get(p!!).group!!.maxSize)
                joinRequest!!.addProperty("test", pendinggroupList!!.get(p!!).group!!.test)
                joinGroup(joinRequest)

            }



        }
    }
    private fun setScoreApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            // progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.setScoreParameter(mpref!!.getAccessToken("").toString(),jsonObject)
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

                            mpref!!.setIsQuestionnaireName("true")


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
    private fun joinGroup(jsonObject: JsonObject){

        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.joinGroupParameter(mpref!!.getAccessToken(""), jsonObject)
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
                            groupJoinList()
                            pendinggroupList()
                            dialog!!.dismiss()


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

    private fun showDialogTeamList() {




    }
}