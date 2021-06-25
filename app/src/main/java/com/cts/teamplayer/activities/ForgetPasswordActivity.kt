package com.cts.teamplayer.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ForgetPasswordActivity: AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)


        findId()
    }

    private fun findId() {
        btn_send.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_send->{
                if (edit_email.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        this@ForgetPasswordActivity,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    var forgotRequest: JsonObject = JsonObject()
                    forgotRequest!!.addProperty("email", edit_email.text.toString().trim())
                    forgotpassApi(forgotRequest)
                }

            }
        }
    }
    private fun forgotpassApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@ForgetPasswordActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.forgotpass(jsonObject)
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    // Log.e("log",response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            Log.d("response", response.body()!!.toString())
                            val jsonObject = JSONObject(response.body().toString())
                            Toast.makeText(this@ForgetPasswordActivity, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                            finish()

                            //    mpref!!.setToken(token)
                            // Log.d("usertype",user_type);
                            /*       mpref!!.setToken(token)
                                   mpref!!.setUserType(usertype)
                                   mpref!!.setUserId(usertype)
                                  */

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                    else if(response.code() ==500){
                        Toast.makeText(this@ForgetPasswordActivity, "Internal server error", Toast.LENGTH_LONG).show()
                    }
                    else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(InputStreamReader(response.errorBody()!!.byteStream()))
                            var line=reader.readLine()
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
                            Toast.makeText(this@ForgetPasswordActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@ForgetPasswordActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@ForgetPasswordActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(this@ForgetPasswordActivity, resources.getString(R.string.please_check_internet), Toast.LENGTH_LONG)
                .show()
        }
    }

}
