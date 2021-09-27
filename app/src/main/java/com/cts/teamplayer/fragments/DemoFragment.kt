package com.cts.teamplayer.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.models.FaqResultResponse
import com.cts.teamplayer.models.SendInviteResponse
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import kotlinx.android.synthetic.main.fragment_demo_group.view.*
import kotlinx.android.synthetic.main.fragment_faq.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class DemoFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_demo_group, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        finid()
        return v
    }

    private fun finid() {
        v.edit_invite_participat_email_im.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edit_invite_participat_email_im -> {
                if (edit_invite_participat_email_im.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.enter_password_length),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var loginRequest: JsonObject = JsonObject()
                    loginRequest!!.addProperty("email", edit_user_name.text.toString().trim())
                    loginRequest!!.addProperty("password", edit_password.text.toString().trim())
                    sendInviteApi(loginRequest)


                }
            }
        }
    }
    private fun sendInviteApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<SendInviteResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.sendInviteParameter(mpref!!.getAccessToken(""),jsonObject)
            call!!.enqueue(object : Callback<SendInviteResponse> {
                override fun onResponse(
                    call: Call<SendInviteResponse>,
                    response: retrofit2.Response<SendInviteResponse>
                ) {
                    progress.dismiss()
                    Log.e("log",response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)

                     //   question_one  =response.body()!!.data!!.get(0)!!.question


                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
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

                override fun onFailure(call: Call<SendInviteResponse>, t: Throwable) {
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