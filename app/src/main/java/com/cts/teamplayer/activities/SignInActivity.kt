package com.cts.teamplayer.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.CustomParticipantTeamList
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SignInActivity: AppCompatActivity() , View.OnClickListener {

    var passwordNotVisible = 0
    private var mpref: TeamPlayerSharedPrefrence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        findId()
    }
      private fun findId() {
        mpref = TeamPlayerSharedPrefrence.getInstance(this)

        val first = "Don't have an account? "
        val next = "<font color='#069fbe'>Sign UP</font>"
        tv_dont_account_signup.setText(Html.fromHtml(first + next))
        btn_login.setOnClickListener(this)
        iv_user_password.setOnClickListener(this)
        tv_forgot_pass.setOnClickListener(this)
        tv_dont_account_signup.setOnClickListener(this)
        ll_skip_sign_in_screen.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                if (!Utility.isValidEmail(edit_user_name.text.toString().trim())) {
                    Toast.makeText(
                        this@SignInActivity,
                        getString(R.string.enter_valid_email),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (edit_password.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        this@SignInActivity,
                        resources.getString(R.string.enter_password_length),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var loginRequest: JsonObject = JsonObject()
                    loginRequest!!.addProperty("email", edit_user_name.text.toString().trim())
                    loginRequest!!.addProperty("password", edit_password.text.toString().trim())
                    loginApi(loginRequest)


                }
            }
            R.id.tv_forgot_pass -> {
                val i = Intent(this@SignInActivity, ForgetPasswordActivity::class.java)
                startActivity(i)
            }R.id.ll_skip_sign_in_screen -> {
            finish()
            }
            R.id.tv_dont_account_signup -> {
                val i = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(i)
            }
            R.id.iv_user_password -> {
                if (passwordNotVisible == 0) {
                    edit_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    iv_user_password.setImageResource(R.drawable.password_show_new)
                    passwordNotVisible = 1
                } else {
                    edit_password.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    iv_user_password.setImageResource(R.drawable.password_hidden)
                    passwordNotVisible = 0
                }
            }
        }
    }
    private fun loginApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignInActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.login(jsonObject)
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
                                this@SignInActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()

                            val token = jsonObject.getJSONObject("data").optString("token")
                            val role = jsonObject.getJSONObject("data").optString("role")
                            mpref!!.setAccessToken(token)
                            mpref!!.setRoal(role)
                            val i = Intent(this@SignInActivity, MainActivity::class.java).addFlags(
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
                            this@SignInActivity,
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
                            Toast.makeText(this@SignInActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignInActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignInActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignInActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
}