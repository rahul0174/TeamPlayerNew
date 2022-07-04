package com.cts.teamplayer.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
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

class RequestDemoActivity : AppCompatActivity() , View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var ischeck = false
    var text_your_role:String?=null
    var text_no_of_empl:String?=null
    var text_hours:String?=null
    var tv_mint:String?=null
    var text_date:String?=null

    @RequiresApi(Build.VERSION_CODES.N)
    val myCalendar: Calendar = Calendar.getInstance()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_demo)

        findId()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun findId() {

        val first = "Accept all "
        val second = "<font color='#069FBE'>Privacy & Policy</font>"
        simpleCheckBox_req_a_demo.setText(Html.fromHtml(first + second))

        btn_submit_in_request_demo.setOnClickListener(this)
        simpleCheckBox_req_a_demo.setOnClickListener(this)
        img_back_request_a_demo.setOnClickListener(this)
        /* {"first_name":"Kul Bhushan","last_name":"Gupta","email":"mohitch@yopmail.com","organization_name":"CTS","phone":9999999999,"agreeTerms":true,"agreePrivacy":true,"user_role":"1","no_of_employees":"1-20","selected_date":""}
      */
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        rl_open_calender_in_demo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                DatePickerDialog(
                    this@RequestDemoActivity, date, myCalendar[Calendar.YEAR],
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
        tv_date_select_in_request_demo_page.setText(sdf.format(myCalendar.time))
    }

    override fun onClickItem(position: Int, requestcode: Int) {

    }
    private fun opentime(){
        val languages = resources.getStringArray(R.array.hours)
        val min = resources.getStringArray(R.array.minuet)
        val your_role = resources.getStringArray(R.array.your_role)
        val number_of_employee = resources.getStringArray(R.array.number_of_employee)

        // access the spinner
        val adapter_hours = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, languages)

        val adapter_mint = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, min)

        val adapter_your_role = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, your_role)

        val adapter_number_of_employee = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, number_of_employee)


        spinner_hours.adapter = adapter_hours

        spinner_hours.onItemSelectedListener = object :
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

        spinner_your_role.adapter = adapter_your_role
        spinner_your_role.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                text_your_role=position.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }

        }

        spinner_num_of_employee.adapter = adapter_number_of_employee
        spinner_num_of_employee.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                text_no_of_empl=number_of_employee[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }

        }

        spinner_mint.adapter = adapter_mint
        spinner_mint.onItemSelectedListener = object :
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
            R.id.img_back_request_a_demo -> {
               onBackPressed()
            }
            R.id.btn_submit_in_request_demo -> {
                if (checkVAlidation()) {
                    var demoRequest: JsonObject = JsonObject()
                    demoRequest!!.addProperty("first_name", edit_ful_name_rq.text.toString().trim())
                    demoRequest!!.addProperty("last_name", edit_ful_name_rq.text.toString().trim())
                    demoRequest!!.addProperty("email", edit_email_rq.text.toString().trim())
                    demoRequest!!.addProperty(
                        "organization_name",
                        edit_company_name_rq.text.toString().trim()
                    )
                    demoRequest!!.addProperty("phone", edit_phone_num_rq.text.toString().trim())
                    demoRequest!!.addProperty("agreeTerms", ischeck)
                    demoRequest!!.addProperty("agreePrivacy", ischeck)
                    demoRequest!!.addProperty(
                        "user_role",
                        text_your_role
                    )
                    demoRequest!!.addProperty(
                        "no_of_employees",
                        text_no_of_empl
                    )
                    demoRequest!!.addProperty("selected_date", text_date+" "+text_hours+":"+tv_mint)
                    requestDemoApi(demoRequest)
                }
            }
        }
    }
    private fun requestDemoApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.remoRequest(jsonObject)
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
                                this@RequestDemoActivity,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            /*

                            val token = jsonObject.getJSONObject("data").optString("token")
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
                            this@RequestDemoActivity,
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
                            Toast.makeText(this@RequestDemoActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@RequestDemoActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@RequestDemoActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@RequestDemoActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun checkVAlidation(): Boolean {

        if (edit_ful_name_rq.text.toString().trim().length == 0) {
            Toast.makeText(this@RequestDemoActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
            return false
        }
        else  if (edit_company_name_rq.text.toString().trim().length == 0) {
            Toast.makeText(this@RequestDemoActivity, getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_phone_num_rq.text.toString().trim().length == 0) {
            Toast.makeText(this@RequestDemoActivity, getString(R.string.phone_num), Toast.LENGTH_SHORT).show()
            return false
        } else  if (!Utility.isValidEmail(edit_email_rq.text.toString().trim())) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.enter_valid_email),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        else  if (text_your_role.equals(null)) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.enter_user_role),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else  if (text_no_of_empl.equals(null)) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.enter_number_employee),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (text_no_of_empl.equals(null)) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.enter_number_employee),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (text_hours.equals(null)) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.select_hours),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (tv_mint.equals(null)) {
            Toast.makeText(
                this@RequestDemoActivity,
                getString(R.string.select_minet),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}