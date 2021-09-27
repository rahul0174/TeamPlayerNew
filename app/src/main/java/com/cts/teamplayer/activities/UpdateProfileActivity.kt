package com.cts.teamplayer.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.OccupationListAdapter
import com.cts.teamplayer.adapters.SectorListAdapter
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants.ADDRESS
import com.cts.teamplayer.util.MyConstants.EMAIL
import com.cts.teamplayer.util.MyConstants.FIRST_NAME
import com.cts.teamplayer.util.MyConstants.LAST_NAME
import com.cts.teamplayer.util.MyConstants.PHONE_NUM
import com.cts.teamplayer.util.MyConstants.PROFESSION
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edit_password
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.dialog_country.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class UpdateProfileActivity: AppCompatActivity() , View.OnClickListener,ItemClickListner {
    var profession_id:String?=null
    private var mpref: TeamPlayerSharedPrefrence? = null
    var sector_id:String?=null
    var sectotList: java.util.ArrayList<OccupationsList>? = null
    private var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        findId()
    }

    private fun findId() {
        mpref = TeamPlayerSharedPrefrence.getInstance(this)
        iv_back_profile.setOnClickListener(this)
        iv_profession.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        rl_profession_open_list.setOnClickListener(this)
        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)
        edit_first_name_profile.text= Editable.Factory.getInstance().newEditable(intent.getStringExtra(FIRST_NAME))
        edit_last_name_profile.text= Editable.Factory.getInstance().newEditable(intent.getStringExtra(
            LAST_NAME))
        edit_phone_num_update.text= Editable.Factory.getInstance().newEditable(intent.getStringExtra(
            PHONE_NUM))
        tv_email_update.text= (intent.getStringExtra(EMAIL))
        tv_address_update.text= (intent.getStringExtra(ADDRESS))
        tv_profession_update.text= (intent.getStringExtra(PROFESSION))


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back_profile -> {
            finish()
            }R.id.rl_profession_open_list -> {
            sectorList()
            } R.id.tv_profession_update -> {

            } R.id.btn_save -> {
                if(checkVAlidation()){
                    /*{"":"Mohit","last_name":"Chauhan","phone":"1217026104","occupation":"0"}*/
                    var loginRequest: JsonObject = JsonObject()
                    loginRequest!!.addProperty("first_name", edit_first_name_profile.text.toString().trim())
                    loginRequest!!.addProperty("last_name", edit_last_name_profile.text.toString().trim())
                    loginRequest!!.addProperty("phone", edit_phone_num_update.text.toString().trim())
                    loginRequest!!.addProperty("occupation", sector_id)
                    profileUpdateReq(loginRequest)
                }
            }
        }
    }
    private fun profileUpdateReq(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@UpdateProfileActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.updateProfileRequest(mpref!!.getAccessToken(""),jsonObject)
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
                                this@UpdateProfileActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@UpdateProfileActivity,
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
                            Toast.makeText(this@UpdateProfileActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@UpdateProfileActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun sectorList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@UpdateProfileActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<OccupationResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getOccupation()
            call!!.enqueue(object : Callback<OccupationResponse> {
                override fun onResponse(
                    call: Call<OccupationResponse>,
                    response: retrofit2.Response<OccupationResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            sectotList = response.body()!!.data as ArrayList<OccupationsList>?
                            sectorDialog(sectotList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@UpdateProfileActivity,
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
                            Toast.makeText(this@UpdateProfileActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<OccupationResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@UpdateProfileActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun sectorDialog(list: ArrayList<OccupationsList>) {
        dialog = android.app.Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
      //  dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@UpdateProfileActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
        val   countryListAdapter =  OccupationListAdapter(this@UpdateProfileActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = countryListAdapter

        dialog!!.show()
    }
    private fun checkVAlidation(): Boolean {
        if (edit_first_name_profile.text.toString().trim().length === 0) {
            Toast.makeText(this@UpdateProfileActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
            return false
        }
        else  if (edit_last_name_profile.text.toString().trim().length === 0) {
            Toast.makeText(this@UpdateProfileActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_phone_num_update.text.toString().trim().length === 0) {
            Toast.makeText(this@UpdateProfileActivity, getString(R.string.phone_num), Toast.LENGTH_SHORT).show()
            return false
        } else  if (sector_id.equals("")) {
            Toast.makeText(
                this@UpdateProfileActivity,
                getString(R.string.select_profession),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode==2){
            sector_id=sectotList!!.get(position).id.toString()
            tv_profession_update.text=sectotList!!.get(position).text
            dialog!!.dismiss()
        }
    }
}

