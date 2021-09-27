package com.cts.teamplayer.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.InviteeListAdapter
import com.cts.teamplayer.adapters.ParticipantsGroupInList
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants.DELETE_INVITEE_EMAIL
import com.cts.teamplayer.util.MyConstants.RESEND_INVITEE_EMAIL
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_invitees_list.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_demo_group.recyler_participant_on_group
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class InviteeListActivity : AppCompatActivity() , View.OnClickListener, ItemClickListner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitees_list)
        inviteeListsApi(intent.getStringExtra("GROUP_ID").toString())
        findId()
    }
    private fun findId() {
        img_back_invitee.setOnClickListener(this)

    }

    fun setInviteeList(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_invitee_list.layoutManager = manager
        val   groupListAdapter =  InviteeListAdapter(this, inviteeListDataItem, this)
        recycler_invitee_list.adapter = groupListAdapter
    }


    var inviteeListDataItem: java.util.ArrayList<InviteeListDataItem>? = null
    private fun inviteeListsApi(group_id: String) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<InviteeListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.inviteeListParameter(
                TeamPlayerSharedPrefrence.getInstance(this).getAccessToken(""), intent.getStringExtra(
                    "GROUP_ID"
                ).toString()
            )
            call!!.enqueue(object : Callback<InviteeListResponse> {
                override fun onResponse(
                    call: Call<InviteeListResponse>,
                    response: retrofit2.Response<InviteeListResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {

                        inviteeListDataItem =
                            response.body()!!.data as ArrayList<InviteeListDataItem>?
                        setInviteeList()

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@InviteeListActivity,
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
                            Toast.makeText(this@InviteeListActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@InviteeListActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<InviteeListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@InviteeListActivity,
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back_invitee -> {
                onBackPressed()
            }
        }
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode==DELETE_INVITEE_EMAIL){

        }
        if(requestcode==RESEND_INVITEE_EMAIL){
            var sendRequest: JsonObject = JsonObject()
            sendRequest!!.addProperty("group_id", intent.getStringExtra("GROUP_ID"))
            sendRequest!!.addProperty(
                "email",
               inviteeListDataItem!!.get(position).email
            )
            sendInviteApi(sendRequest)
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
            call = apiInterface!!.sendInviteParameter(TeamPlayerSharedPrefrence.getInstance(this).getAccessToken(""), jsonObject)
            call!!.enqueue(object : Callback<SendInviteResponse> {
                override fun onResponse(
                    call: Call<SendInviteResponse>,
                    response: retrofit2.Response<SendInviteResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)

                        Toast.makeText(
                            this@InviteeListActivity,
                            response.body()!!.message,
                            Toast.LENGTH_LONG
                        ).show()


                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@InviteeListActivity,
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
                            Toast.makeText(this@InviteeListActivity, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@InviteeListActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SendInviteResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@InviteeListActivity,
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
