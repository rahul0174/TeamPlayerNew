package com.cts.teamplayer.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.SignUpActivity
import com.cts.teamplayer.activities.UpdateProfileActivity
import com.cts.teamplayer.customui.CustomTextView
import com.cts.teamplayer.models.UserProfileDetailsNResponse
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.MyConstants.ADDRESS
import com.cts.teamplayer.util.MyConstants.EMAIL
import com.cts.teamplayer.util.MyConstants.FIRST_NAME
import com.cts.teamplayer.util.MyConstants.LAST_NAME
import com.cts.teamplayer.util.MyConstants.PHONE_NUM
import com.cts.teamplayer.util.MyConstants.PROFESSION
import com.cts.teamplayer.util.MyConstants.USERS_IMAGE
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.dialog_add_to_team.*
import kotlinx.android.synthetic.main.fragment_participants_profile.*
import kotlinx.android.synthetic.main.fragment_participants_profile.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ParticipantsProfileFragment : Fragment(),View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var first_name:String?=null
    var last_name:String?=null
    var phone_num:String?=null
    var profession:String?=null
    var email:String?=null
    var address:String?=null
    var dialog:Dialog?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_participants_profile, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        v.rl_profile_edit.setOnClickListener(this)
        v.btn_view_cv.setOnClickListener(this)
        if(mpref!!.getAccessToken("")!!.equals(null)){

        }else{
            vendorDetailByToken(mpref!!.getAccessToken("").toString())
        }


        return v
    }
    var cv_url:String?=null
    private fun vendorDetailByToken(token: String) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<UserProfileDetailsNResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getUserDetailByToken(token)
            call!!.enqueue(object : Callback<UserProfileDetailsNResponse> {
                override fun onResponse(
                    call: Call<UserProfileDetailsNResponse>,
                    response: retrofit2.Response<UserProfileDetailsNResponse>
                ) {
                    progress.dismiss()
                    Log.e("log",response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                      //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)
                     if(response.body()!!.data!!.im==null){
                         tv_im_id_user_profile.visibility=View.GONE
                         ll_im_id.visibility=View.GONE
                         tv_im_id_user_profile.text="1234567"

                     }else{
                         tv_im_id_user_profile.visibility=View.VISIBLE
                         ll_im_id.visibility=View.VISIBLE
                         tv_im_id_user_profile.text=response.body()!!.data!!.im.toString()
                     }
                             tv_name_in_user_profile.text=response.body()!!.data!!.title+" "+response.body()!!.data!!.firstName+" "+response.body()!!.data!!.lastName
                        tv_last_name_in_user_profile.text=response.body()!!.data!!.lastName
                        tv_country_in_user_profile.text=response.body()!!.data!!.countryData!!.name
                        tv_state_in_user_profile.text=response.body()!!.data!!.stateData!!.name

                        if(response.body()!!.data!!.cityData!!.name!=null){
                            tv_city_in_user_profile.text=response.body()!!.data!!.cityData!!.name
                        }

                        tv_email_in_user_profile.text=response.body()!!.data!!.email
                        tv_phone_in_user_profile.text=response.body()!!.data!!.phone
                        if (response.body()!!.data!!.organizationName.toString()!="null"){
                            ll_company_name_insignup.visibility=View.VISIBLE
                            tv_company_name_in_signin.text=response.body()!!.data!!.organizationName.toString()
                        }else{

                            ll_company_name_insignup.visibility=View.GONE
                        }
                        tv_user_name_in_profile.text=response.body()!!.data!!.title+" "+response.body()!!.data!!.firstName+" "+response.body()!!.data!!.lastName

                        first_name=response.body()!!.data!!.firstName
                        last_name=response.body()!!.data!!.lastName
                        phone_num=response.body()!!.data!!.phone
                        email=response.body()!!.data!!.email
                        address=response.body()!!.data!!.addressLine1
                        profession=response.body()!!.data!!.occupationData!!.name
                   //     profession=response.body()!!.
                        cv_url=USERS_IMAGE+response.body()!!.data!!.cv
                       tv_profession_in_user_profile.text=response.body()!!.data!!.occupationData!!.name

                        if(response.body()!!.data!!.cv==null){

                            btn_view_cv.visibility=View.GONE

                        }else{
                            Glide.with(activity!!)
                                .load(USERS_IMAGE+response.body()!!.data!!.cv)
                                /*  .load("https://34.204.201.39/HealthcareApplication/uploadedFiles/gijEaKes_1643028174729.jpg")
                                */  .override(60, 60)
                                .fitCenter() // scale to fit entire image within ImageView
                                .into(iv_user_image)
                            btn_view_cv.visibility=View.VISIBLE
                        }




                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }/*else if(response.code()==401){
                        mpref!!.setToken("")
                        mpref!!.clear()
                        val i = Intent(this@VendorProfileEditActivity, CountryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(i)
                    }*/
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

                override fun onFailure(call: Call<UserProfileDetailsNResponse>, t: Throwable) {
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_profile_edit -> {
                startActivity(Intent(activity, UpdateProfileActivity::class.java).putExtra(FIRST_NAME,first_name).putExtra(
                    LAST_NAME,last_name).putExtra(PHONE_NUM,phone_num).putExtra(EMAIL,email).putExtra(ADDRESS,address).putExtra(PROFESSION,profession))
            }
            R.id.btn_view_cv -> {
                showDialog()
         }
        }

    }

    override fun onResume() {
        super.onResume()
        if(mpref!!.getAccessToken("")!!.equals(null)){

        }else{
            vendorDetailByToken(mpref!!.getAccessToken("").toString())
        }
    }
    private fun showDialog() {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(R.layout.dialog_view_cv)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)

        val img_show_cv = dialog!!.findViewById(R.id.img_show_cv) as ImageView

        Glide.with(activity!!)
            .load(cv_url)
            .fitCenter() // scale to fit entire image within ImageView
            .into(img_show_cv);
        dialog!!.show()

    }

}