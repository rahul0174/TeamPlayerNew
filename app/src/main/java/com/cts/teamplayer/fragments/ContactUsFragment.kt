package com.cts.teamplayer.fragments

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.edit_user_name
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_contact_us.view.*
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

class ContactUsFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var text_hours:String?=null
    var tv_mint:String?=null
    var text_date:String?=null
    @RequiresApi(Build.VERSION_CODES.N)
    val myCalendar: Calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contact_us, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        findId()
        return v
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun findId() {
       v.btn_submit_in_contact_us.setOnClickListener(this)

        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        v.rl_open_calender_in_contact_us.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                DatePickerDialog(
                    activity!!, date, myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        })
        opentime()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text_date=sdf.format(myCalendar.time).toString()
        tv_date_select_in_contact_us_page.setText(sdf.format(myCalendar.time))
    }
    private fun opentime(){
        val languages = resources.getStringArray(R.array.hours)
        val min = resources.getStringArray(R.array.minuet)

        // access the spinner
        val adapter_hours = ArrayAdapter(activity!!,
            android.R.layout.simple_spinner_item, languages)

        val adapter_mint = ArrayAdapter(activity!!,
            android.R.layout.simple_spinner_item, min)

        v.spinner_hours_in_contact_us.adapter = adapter_hours

        v.spinner_hours_in_contact_us.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                text_hours=languages[position].toString()
               }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

        v.spinner_mint_in_contact_us.adapter = adapter_mint
        v.spinner_mint_in_contact_us.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                tv_mint=min[position].toString()

                  }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }

        }



    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit_in_contact_us -> {
                if (edit_ful_name_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.full_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else  if (!Utility.isValidEmail(edit_email_in_contact.text.toString().trim())) {
                    Toast.makeText(
                        activity!!,
                        getString(R.string.enter_valid_email),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (edit_phone_num_in_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.phone_num),
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (edit_subject_in_contact_us.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.enter_subject),
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (edit_message_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.enter_message),
                        Toast.LENGTH_SHORT
                    ).show()
                } else  if (text_hours.equals(null)) {
                    Toast.makeText(
                        activity!!,
                        getString(R.string.select_hours),
                        Toast.LENGTH_SHORT
                    ).show()

                }else  if (tv_mint.equals(null)) {
                    Toast.makeText(
                        activity!!,
                        getString(R.string.select_minet),
                        Toast.LENGTH_SHORT
                    ).show()
                }else{

                  /*  {"name":"Kul Bhushan Gupta","email":"kb.gupta10@gmail.com","phone":"9999999999","subject":"Test Contact","message_text":"Hi Admin","selected_date":"2021-07-28 14:00"}
                 */   var demoRequest: JsonObject = JsonObject()
                    demoRequest!!.addProperty("name", edit_ful_name_contact.text.toString().trim())
                    demoRequest!!.addProperty("email", edit_email_in_contact.text.toString().trim())
                    demoRequest!!.addProperty("phone", edit_phone_num_in_contact.text.toString().trim())
                    demoRequest!!.addProperty("subject", edit_subject_in_contact_us.text.toString().trim())
                    demoRequest!!.addProperty("message_text", edit_message_contact.text.toString().trim())
                    demoRequest!!.addProperty("selected_date", text_date+" "+text_hours+":"+tv_mint)
                    contactUsApi(demoRequest)
                }

            }
        }
    }
    private fun contactUsApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.contactUsRequest(jsonObject)
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

                             /*   val token = jsonObject.getJSONObject("data").optString("token")
                                mpref!!.setAccessToken(token)
                                val i = Intent(activity!!, MainActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(i)*/

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


}