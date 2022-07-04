package com.cts.teamplayer.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.MaltipleInviteAdapter
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_maltiple_invite_email.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MaltipleInviteEmailActivity : AppCompatActivity() , View.OnClickListener, ItemClickListner {
    val list: ArrayList<String> = arrayListOf<String>()
    var remaining:Int?=null
    var numberOfLines = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maltiple_invite_email)
        ll_add_more_participants.setOnClickListener(this)
        btn_sent_multiple_invite.setOnClickListener(this)
        img_back_multiinvite.setOnClickListener(this)
        remaining=intent.getIntExtra("REMAINING",0).toInt()

    }

    fun addEmail(number:Int){
         if(numberOfLines>remaining!!){
            Toast.makeText(
                this@MaltipleInviteEmailActivity,
                "You can only invite "+list.size +" participants. Please buy more questionaire in order to invite more participants." ,
                Toast.LENGTH_SHORT
            ).show()
        }
       else if(number==1){
            ll_1.visibility=View.VISIBLE
        }
        else if(number==2){
            ll_2.visibility=View.VISIBLE
        }else if(number==3){
            ll_3.visibility=View.VISIBLE
        }else if(number==4){
            ll_4.visibility=View.VISIBLE
        }else if(number==5){
            ll_5.visibility=View.VISIBLE
        }else if(number==6){
            ll_6.visibility=View.VISIBLE
        }else if(number==7){
            ll_7.visibility=View.VISIBLE
        }else if(number==8){
            ll_8.visibility=View.VISIBLE
        }else if(number==9){
            ll_9.visibility=View.VISIBLE
        }else if(number==10){
            ll_10.visibility=View.VISIBLE
        }else if(number==11){
            ll_11.visibility=View.VISIBLE
        }else if(number==12){
            ll_12.visibility=View.VISIBLE
        }else if(number==13){
            ll_13.visibility=View.VISIBLE
        }else if(number==14){
            ll_14.visibility=View.VISIBLE
        }
        else{
            Toast.makeText(
                this@MaltipleInviteEmailActivity,
                "You can only invite 15 participants at a time." ,
                Toast.LENGTH_SHORT
            ).show()

            Toast.makeText(
                this@MaltipleInviteEmailActivity,
                "You can only invite"+list.size +"participants. Please buy more questionaire in order to invite more participants." ,
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_add_more_participants -> {
          /*      numberofEmailEnter++
                addEmail(numberofEmailEnter)*/
                 if (!Utility.isValidEmail(edit_email_in_maltiple_email.text.toString().trim())) {
                    Toast.makeText(
                        this@MaltipleInviteEmailActivity,
                        getString(R.string.enter_valid_email),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {


                    if(list.size>14){
                        Toast.makeText(
                            this@MaltipleInviteEmailActivity,
                            "You can only invite 15 participants at a time." ,
                            Toast.LENGTH_SHORT
                        ).show()

                        Toast.makeText(
                            this@MaltipleInviteEmailActivity,
                            "You can only invite"+list.size +"participants. Please buy more questionaire in order to invite more participants." ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if(list.size<14){
                        remaining=intent.getIntExtra("REMAINING",0).toInt()
                        if((list.size+1)>remaining!!){
                            Toast.makeText(
                                this@MaltipleInviteEmailActivity,
                                "You can only invite "+list.size +" participants. Please buy more questionaire in order to invite more participants." ,
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            list.add(edit_email_in_maltiple_email.text.toString().trim())
                            edit_email_in_maltiple_email.text!!.clear()
                            setBenchMarkListInRecycler()

                        }

                    }

                }
            }
            R.id.img_back_multiinvite -> {
                onBackPressed()

            }
            R.id.btn_sent_multiple_invite -> {
                /*    {"group_id":"31","emails":["test000@yopmail.com","test001"]}*/

            /* if(edit_email_in_maltiple_email.text!!.isNotEmpty()){

             }
                list.add(edit_email_in_maltiple_email.text.toString().trim())*/
                if (list.size > 0) {
                    var jsonObject = JsonObject()

                    try {
                        // jsonString is a string variable that holds the JSON
                        val itemArray = JsonArray()
                        for (i in 0 until list.size) {

                            itemArray.add(list.get(i).toString())
                            //     Log.e("json", itemArray)
                        }


                        jsonObject!!.add("emails", itemArray)
                        jsonObject.addProperty("group_id", intent.getStringExtra("GROUP_ID"))

                        Log.e("TAG", "onClick: " + jsonObject.toString())
                        sentInviteApi(jsonObject)
                        ///   loginApi(loginRequest)
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        this@MaltipleInviteEmailActivity,
                        "Please atleast one enter email-id",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
        }
    }
    private fun sentInviteApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@MaltipleInviteEmailActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.sendMultipleInviteParameter(
                TeamPlayerSharedPrefrence.getInstance(
                    this
                ).getAccessToken(""), jsonObject
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
                                this@MaltipleInviteEmailActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            onBackPressed()
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
                            this@MaltipleInviteEmailActivity,
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
                            Toast.makeText(
                                this@MaltipleInviteEmailActivity,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@MaltipleInviteEmailActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@MaltipleInviteEmailActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@MaltipleInviteEmailActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setBenchMarkListInRecycler(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_maltiple_invity.layoutManager = manager
        val   groupListAdapter =  MaltipleInviteAdapter(this, list, this)
        recycler_maltiple_invity.adapter = groupListAdapter
        groupListAdapter.notifyDataSetChanged()
        tv_num_of_participants.text=list.size.toString()
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        TODO("Not yet implemented")
    }

    fun sendJson(){
        for(item in list){
            val student1 = JSONObject()
            try {

                student1.put("id", list.toString())
                student1.put("name", "NAME OF STUDENT")

            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            val jsonArray = JSONArray()

            jsonArray.put(student1)
        }

    }

}