package com.cts.teamplayer.activities

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.CustomParticipantTeamList
import com.cts.teamplayer.adapters.ParticipantsGroupInList
import com.cts.teamplayer.adapters.SubGroupAdapter
import com.cts.teamplayer.customui.CustomTextView
import com.cts.teamplayer.fragments.BriefQuestionnaireFragment
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.FunctionHelper
import com.cts.teamplayer.util.MyConstants.ADD_TO_TEAM
import com.cts.teamplayer.util.MyConstants.MANAGE_TEAM_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.PARTICIPANTS_TEAM_REQUEST_CODE
import com.cts.teamplayer.util.MyConstants.SEND_REMINDER_REQUEST_CODE
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_invitees_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.dialog_add_to_team.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.view.*
import kotlinx.android.synthetic.main.fragment_request_demo.*
import kotlinx.android.synthetic.main.fragment_request_demo.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


class GroupManageActivity: AppCompatActivity() , View.OnClickListener,ItemClickListner {
    var name:String?=null
    var remaining:Int?=null
    var user_name:String?=null
    var ueser_id:String?=null
    var manage_text_group:String?=null
    var dialog:Dialog?=null
    var dialog1:Dialog?=null
    lateinit var homeFragment: androidx.fragment.app.Fragment
    var spinner_team_list:Spinner?=null
    var numbers: ArrayList<UserListItem> = ArrayList()

    var groupList: java.util.ArrayList<GroupListItem>? = null
    var countryFilterList : ArrayList<SurveyParticipantsItem>? = null
    private var mpref: TeamPlayerSharedPrefrence? = null
    var subGroupList: java.util.ArrayList<SubGroupList>? = null
    var surveyGroupList: java.util.ArrayList<SurveyParticipantsItem>? = null
    var participantsGroupInList: ParticipantsGroupInList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_demo_group)
        hideKeyBoard(this)
        findId()
    }

    private fun findId() {
        mpref = TeamPlayerSharedPrefrence.getInstance(this)
     //   edit_invite_participat_email_im.setOnClickListener(this)
        rl_click_manage_txt_group.setOnClickListener(this)
        rl_click_manage_cts_brief.setOnClickListener(this)
        tv_create_team.setOnClickListener(this)
        img_brief_ques.setOnClickListener(this)
        tv_invitee_click.setOnClickListener(this)
     //   rl_invitee.setOnClickListener(this)
        QuestionnaireGroupDetailsApi(intent.getStringExtra("GROUP_ID").toString())
        SubGroupListApi()
        groupList()


        search_participant.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterParticipant(newText.toString())
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

       /* edit_invite_participat_email_im.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendData()
            }
        })*/
        rl_invitee.setOnClickListener {
            sendData()
            /*         participantsGroupInList!!.filter(
                         seachView.text.toString().trim { it <= ' ' })*/
        }


        tv_search_item.setOnClickListener {
            filter(seachView.text.toString())
   /*         participantsGroupInList!!.filter(
                seachView.text.toString().trim { it <= ' ' })*/
        }

    }
    private fun filterParticipant(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<SurveyParticipantsItem> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in this!!.surveyGroupList!!) {
            //if the existing elements contains the search input
            if (s!!.userName!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = participantsGroupInList!!.filterList(filterdNames)

    }
    private fun sendData(){
        if(remaining==0){
            dialog1 = Dialog(this)
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
                dialog1!!.dismiss()

            /*    startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "GROUP_ID", intent.getStringExtra(
                            "GROUP_ID"
                        )
                    ).putExtra(
                        "REMAINING", remaining
                    ))*/
            }
        }else{
            startActivity(
                Intent(this, MaltipleInviteEmailActivity::class.java).putExtra(
                    "GROUP_ID", intent.getStringExtra(
                        "GROUP_ID"
                    )
                ).putExtra(
                    "REMAINING", remaining
                ))
        }

    }
    fun hideKeyBoard(activity: Activity): Boolean {
        try {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            /*R.id.edit_invite_participat_email_im -> {
                if (edit_invite_participat_email_im.text!!.toString()
                        .trim { it <= ' ' }.length == 0
                ) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.email),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var sendRequest: JsonObject = JsonObject()
                    sendRequest!!.addProperty("group_id", intent.getStringExtra("GROUP_ID"))
                    sendRequest!!.addProperty(
                        "email",
                        edit_invite_participat_email_im.text.toString().trim()
                    )
                    sendInviteApi(sendRequest)
                }
            }*/
            R.id.tv_create_team -> {
                if (edit_team_name.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.enter_team_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var teamRequest: JsonObject = JsonObject()
                    teamRequest!!.addProperty("group_id", intent.getStringExtra("GROUP_ID"))
                    teamRequest!!.addProperty("name", edit_team_name.text.toString().trim())
                    createTeamApi(teamRequest)
                }
            }
            R.id.rl_click_manage_txt_group -> {
                manage_text_group = "m"
                tv_questionnaire_title.text = "Questionnaire" + " " + name
                rl_click_manage_cts_brief.setBackgroundColor(resources.getColor(R.color.light_blue))
                tv_manage_cts_brief.setTextColor(ContextCompat.getColor(this, R.color.white))

                rl_click_manage_txt_group.setBackgroundColor(resources.getColor(R.color.white))
                tv_manage_text_group.setTextColor(ContextCompat.getColor(this, R.color.light_blue))
            }
            R.id.rl_click_manage_cts_brief -> {
                tv_questionnaire_title.text = ""
                rl_click_manage_cts_brief.setBackgroundColor(resources.getColor(R.color.white))
                tv_manage_cts_brief.setTextColor(ContextCompat.getColor(this, R.color.light_blue))

                rl_click_manage_txt_group.setBackgroundColor(resources.getColor(R.color.light_blue))
                tv_manage_text_group.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            R.id.img_brief_ques -> {
                finish()
            }
            R.id.tv_invitee_click -> {
                startActivity(
                    Intent(this, InviteeListActivity::class.java).putExtra(
                        "GROUP_ID", intent.getStringExtra(
                            "GROUP_ID"
                        )
                    )
                )

            }
        }
    }
    private fun sendInviteApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<SendInviteResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.sendInviteParameter(mpref!!.getAccessToken(""), jsonObject)
            call!!.enqueue(object : Callback<SendInviteResponse> {
                override fun onResponse(
                    call: Call<SendInviteResponse>,
                    response: retrofit2.Response<SendInviteResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)

                       /* edit_invite_participat_email_im.text =
                            Editable.Factory.getInstance().newEditable(
                                ""
                            )*/
                        Toast.makeText(
                            this@GroupManageActivity,
                            response.body()!!.message,
                            Toast.LENGTH_LONG
                        ).show()


                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SendInviteResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
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

    private fun createTeamApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<SendInviteResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.addSubGroupParameter(mpref!!.getAccessToken(""), jsonObject)
            call!!.enqueue(object : Callback<SendInviteResponse> {
                override fun onResponse(
                    call: Call<SendInviteResponse>,
                    response: retrofit2.Response<SendInviteResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        edit_team_name.text = Editable.Factory.getInstance().newEditable("")
                        SubGroupListApi()
                        Toast.makeText(
                            this@GroupManageActivity,
                            response.body()!!.message,
                            Toast.LENGTH_LONG
                        ).show()


                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SendInviteResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
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

    private fun QuestionnaireGroupDetailsApi(group_id: String) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<QuestionnaireGroupDetailResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.questionnaireGroupDetailsParameter(
                mpref!!.getAccessToken(""), intent.getStringExtra(
                    "GROUP_ID"
                ).toString()
            )
            call!!.enqueue(object : Callback<QuestionnaireGroupDetailResponse> {
                override fun onResponse(
                    call: Call<QuestionnaireGroupDetailResponse>,
                    response: retrofit2.Response<QuestionnaireGroupDetailResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)
                        remaining=response.body()!!.data!!.remaining
                        tv_questionnaire_remaining.text= response.body()!!.data!!.remaining.toString()
                        tv_questionnaire_remaining.text =
                            response.body()!!.data!!.remaining.toString() + " questionnaire remaining"
                        tv_questionnaire_title.text = TeamPlayerSharedPrefrence.getInstance(this@GroupManageActivity).getBusinessName("")+" " + response.body()!!.data!!.remaining.toString() + " questionnaire remaining"

                        name = response.body()!!.data!!.surveyGroup!!.name
                        surveyGroupList =
                            response.body()!!.data!!.surveyParticipants as ArrayList<SurveyParticipantsItem>?
                        if(surveyGroupList!!.size>0){
                            tv_which_participant_avalible.text="There are "+surveyGroupList!!.size.toString()+" participants in this questionnaire group.Click a participant's name to view their questionnaire results."
                        }else{
                            tv_which_participant_avalible.text="There are 0 participants in this questionnaire group.Click a participant's name to view their questionnaire results."

                        }
                        setGroupList()

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<QuestionnaireGroupDetailResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
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
                    Log.e("logteam", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)
                        subGroupList = response.body()!!.data as ArrayList<SubGroupList>?
                        setSubGroupList()

                    }/* else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }*/ else {
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SubGroupResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
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
    private fun groupList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
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


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<GroupListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@GroupManageActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    fun setSubGroupList(){
        if(subGroupList!!.size>0){
            img_back_on_manage_team_list_empty.visibility=View.GONE
            recycler_sub_group_list.visibility=View.VISIBLE
            var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler_sub_group_list.layoutManager = manager
            val   groupListAdapter =  SubGroupAdapter(this, subGroupList, this)
            recycler_sub_group_list.adapter = groupListAdapter
        }else{
            img_back_on_manage_team_list_empty.visibility=View.VISIBLE
            recycler_sub_group_list.visibility=View.GONE
        }

    }
    fun setGroupList(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyler_participant_on_group.layoutManager = manager
        participantsGroupInList =  ParticipantsGroupInList(this, surveyGroupList, this)
        recyler_participant_on_group.adapter = participantsGroupInList
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<SurveyParticipantsItem> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in this!!.surveyGroupList!!) {
            //if the existing elements contains the search input
            if (s!!.userName!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = participantsGroupInList!!.filterList(filterdNames)
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode==ADD_TO_TEAM){

            ueser_id=surveyGroupList!!.get(position).profileId
            user_name=surveyGroupList!!.get(position).userName
            showDialog(surveyGroupList!!.get(position).userName.toString())

        }
        if(requestcode==SEND_REMINDER_REQUEST_CODE){
       /*     "id":"2","profile_id":"3627",user_name":"ABCD EFGH","email":"abcd@yopmail.com"}
       */   /*  {"id":"124","":"3715","access":"P","survey_group_id":"50","created_at":"2021-07-30","updated_at":"2021-07-30","expire_at":"2022-07-30","user_name":"Ritesh sinha","email":"riteshtest@yopmail.com","survey_progress":"0","is_expire":false}
       */     var sendReminderRequestRequest: JsonObject = JsonObject()
        /*    addtoteamRequest!!.addProperty("id", surveyGroupList!!.get(position).id)
            addtoteamRequest!!.addProperty("profile_id", surveyGroupList!!.get(position).profileId)
            addtoteamRequest!!.addProperty("access", surveyGroupList!!.get(position).access)
            addtoteamRequest!!.addProperty("survey_group_id",surveyGroupList!!.get(position).surveyProgress )
            addtoteamRequest!!.addProperty("created_at", surveyGroupList!!.get(position).createdAt)
            addtoteamRequest!!.addProperty("updated_at", surveyGroupList!!.get(position).updatedAt)
            addtoteamRequest!!.addProperty("expire_at", surveyGroupList!!.get(position).updatedAt)
            addtoteamRequest!!.addProperty("user_name", surveyGroupList!!.get(position).userName)*/
            sendReminderRequestRequest!!.addProperty("id", surveyGroupList!!.get(position).id)
            sendReminderRequestRequest!!.addProperty(
                "user_name",
                surveyGroupList!!.get(position).userName
            )
            sendReminderRequestRequest!!.addProperty("email", surveyGroupList!!.get(position).email)
        /*    sendReminderRequestRequest!!.addProperty(
                "survey_progress", surveyGroupList!!.get(
                    position
                ).surveyProgress
            )*/
         //   addtoteamRequest!!.addProperty("is_expire", false)

            sendReminderApi(sendReminderRequestRequest)


        }
        if(requestcode==MANAGE_TEAM_REQUEST_CODE){
            val resultIntent = Intent(this, ManageTeamActivity::class.java)
            resultIntent.putExtra(
                "POSITION",
                position
            )
            resultIntent.putExtra(
                "GROUP_ID",
                intent.getStringExtra("GROUP_ID").toString()
            )
            startActivity(resultIntent)

        }
        if(requestcode==PARTICIPANTS_TEAM_REQUEST_CODE){

          /*  {"group_id":"2","subgroup_id":"1","user_id":"1152","user_name":"Anthony Ramirez"}
*/

               /* {
                    "id": "13",
                    "group_id": "50",
                    "subgroup_id": "14",
                    "user_id": "3716",
                    "user_type": "",
                    "created_at": "2021-07-15 10:27:18",
                    "updated_at": null,
                    "user_name": "rahul tamrakar",
                    "email": "rahul@yopmail.com",
                    "survey_progress": "1"
                }*/
            var addtoteamRequest: JsonObject = JsonObject()
            addtoteamRequest!!.addProperty("group_id", subGroupList!!.get(position).groupId)
            addtoteamRequest!!.addProperty(
                "subgroup_id",
                subGroupList!!.get(position).id.toString()
            )
            addtoteamRequest!!.addProperty("user_id", ueser_id)
            addtoteamRequest!!.addProperty("user_name", user_name)
            addtoteamApi(addtoteamRequest)

        }
    }
    private fun addtoteamApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@GroupManageActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.addtoteamRequest(
                TeamPlayerSharedPrefrence.getInstance(this).getAccessToken(
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
                                this@GroupManageActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            dialog!!.dismiss()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@GroupManageActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun sendReminderApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@GroupManageActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.sendReminderRequest(
                TeamPlayerSharedPrefrence.getInstance(this).getAccessToken(
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
                                this@GroupManageActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            //  dialog!!.dismiss()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@GroupManageActivity,
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
                            Toast.makeText(this@GroupManageActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupManageActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@GroupManageActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@GroupManageActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun showDialog(userName: String) {
         dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_add_to_team)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
        val rl_select_team_in_participat = dialog!!.findViewById(R.id.rl_select_team_in_participat) as RelativeLayout
        val iv_cancel_dialog_add_to_team = dialog!!.findViewById(R.id.iv_cancel_dialog_add_to_team) as ImageView
        val edit_user_role_in_req_demo = dialog!!.findViewById(R.id.edit_user_role_in_req_demo) as CustomTextView
        val rl_new_user = dialog!!.findViewById(R.id.rl_new_user) as RelativeLayout
        dialog!!.tv_title_on_create_team_dialog.text="Add "+userName+" To Team"
        iv_cancel_dialog_add_to_team.setOnClickListener { showDialogTeamList()}
        edit_user_role_in_req_demo.setOnClickListener {
            showDialogTeamList()
        }
        rl_new_user.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.show()

    }
    private fun showDialogTeamList() {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_country)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
        val recycler_country_list = dialog!!.findViewById(R.id.recycler_country_list) as RecyclerView
       // val city_search = dialog!!.findViewById(R.id.city_search) as RelativeLayout


        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_country_list.layoutManager = manager
        val   groupListAdapter =  CustomParticipantTeamList(this, subGroupList, this)
        recycler_country_list.adapter = groupListAdapter

        dialog!!.show()

    }


}