package com.cts.teamplayer.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.*
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.dialog_open_image_doc.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.*


class SignUpActivity: AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener
    ,CountryListAdapter.TextBookNow,ItemClickListner
    ,StateListAdapter.TextStateBookNow,CityListAdapter.TextCityBookNow
    ,SectorListAdapter.TextSectorBookNow,OccupationListAdapter.TextBookOccupationNow {

    private var mpref: TeamPlayerSharedPrefrence? = null
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    val PERMISSION_REQUEST_CODE = 10001
    var countryList: java.util.ArrayList<CountryList>? = null
    var sectotList: java.util.ArrayList<SectorList>? = null
    var occupationsList: java.util.ArrayList<OccupationsList>? = null
    var stateList: java.util.ArrayList<StateList>? = null
    var cityList: java.util.ArrayList<CityList>? = null
    private var dialog: Dialog? = null
    var country_id:String?=""
    var company_which_type:String?=""
    var city_id:String?=""
    var state_id:String?=""
    var sector_id:String?=""
    var occupation_id:String?=""
    var text_sector:String?=""
    var text_occupation:String?=""
    var ischeck = false
    var text_your_role:String?=""
    var text_no_of_empl:String?=""
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"

    var stateListAdapter: StateListAdapter? = null
    var countryListAdapter: CountryListAdapter? = null
    var cityListAdapter: CityListAdapter? = null
    var sectorListAdapter: SectorListAdapter? = null

    var occupationListAdapter: OccupationListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mpref = TeamPlayerSharedPrefrence.getInstance(this)

       // uploadImage.setOnClickListener(this)


        findId()
    }

    private fun findId() {

        val first = "Agree to "
        val second = "<font color='#069FBE'>Terms & Conditions</font>"

        val four = "<font color='#069FBE'>Privacy Policy</font>"
        val three = " and "
        simpleCheckBox.setText(Html.fromHtml(first + second + three + four))

        val allready = "Already have an account? "
        val allready_second = "<font color='#069FBE'>Sign In</font>"
        tv_already_account.setText(Html.fromHtml(allready + allready_second))

        val your_role = resources.getStringArray(R.array.your_role)
        val number_of_employee = resources.getStringArray(R.array.number_of_employee)
        val adapter_your_role = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, your_role)

        val adapter_number_of_employee = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, number_of_employee)

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

        rl_upload_image.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        rl_country.setOnClickListener(this)
        rl_state.setOnClickListener(this)
        rl_city.setOnClickListener(this)
        rl_country.setOnClickListener(this)
        rl_sector.setOnClickListener(this)
        rl_occupation.setOnClickListener(this)
        simpleCheckBox.setOnClickListener(this)
        tv_orgnization.setOnClickListener(this)
        tv_signup.setOnClickListener(this)
        tv_already_account.setOnClickListener(this)
        rl_signup_upload_dov_on_click.setOnClickListener(this)
        rl_signup_upload_dov_layout.setOnClickListener(this)
        rl_upload_image_submit.setOnClickListener(this)


    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.rl_upload_image -> {
                openImageDocDialog()

            }R.id.rl_signup_upload_dov_layout -> {
            rl_signup_text_layout.visibility=View.GONE
            btn_submit.visibility=View.GONE

            } R.id.rl_signup_upload_dov_on_click -> {
            rl_upload_image.visibility=View.VISIBLE
            rl_upload_image_submit.visibility=View.VISIBLE
            rl_buy_questionnaire.visibility=View.GONE

            }
            R.id.btn_submit -> {

               /* rl_signup_text_layout.visibility=View.GONE
                iv_signup_first.visibility=View.GONE
                rl_signup_upload_dov_layout.visibility=View.VISIBLE*/
                if (checkVAlidation()) {
                    if(company_which_type.equals("company")){
                        if (checkVAlidation()) {

                            var requestBodyuser = JsonObject()
                            requestBodyuser.addProperty("first_name", edit_full_name.text.toString().trim())
                            requestBodyuser.addProperty("last_name", edit_last_name.text.toString().trim())
                            requestBodyuser.addProperty("email", edit_email.text.toString().trim())
                            requestBodyuser.addProperty("title", edit_title.text.toString().trim())
                            requestBodyuser.addProperty("phone", edit_phone.text.toString().trim())
                        //    requestBodyuser.addProperty("cv", "")
                            requestBodyuser.addProperty("organization_name", edit_orgnization_name.text.toString().trim())
                            requestBodyuser.addProperty(
                                "address_line_1",
                                edit_address.text.toString().trim()
                            )
                            requestBodyuser.addProperty(
                                "address_line_2",
                                edit_landmark.text.toString().trim()
                            )
                            requestBodyuser.addProperty("sector", sector_id)
                          //  requestBodyuser.addProperty("occupation", occupation_id)
                            requestBodyuser.addProperty("country", country_id)
                            requestBodyuser.addProperty("city", city_id)
                            requestBodyuser.addProperty("state", state_id)
                            requestBodyuser.addProperty("zip", edit_zip.text.toString().trim())
                            requestBodyuser.addProperty(
                                "password",
                                edit_password.text.toString().trim()
                            )
                            requestBodyuser.addProperty(
                                "confirm_password",
                                edit_confrim_pass.text.toString().trim()
                            )
                            requestBodyuser.addProperty("agreeTerms", ischeck)
                            requestBodyuser.addProperty("agreePrivacy", ischeck)
                            requestBodyuser.addProperty("no_of_employees", text_no_of_empl)
                            requestBodyuser.addProperty("user_role", text_your_role)

                            userSignup(requestBodyuser)
                        }
                    }else{
                        if (checkVAlidation()) {

                            var requestBodyuser = JsonObject()
                            requestBodyuser.addProperty("first_name", edit_full_name.text.toString().trim())
                            requestBodyuser.addProperty("last_name", edit_last_name.text.toString().trim())
                            requestBodyuser.addProperty("email", edit_email.text.toString().trim())
                            requestBodyuser.addProperty("title", edit_title.text.toString().trim())
                            requestBodyuser.addProperty("phone", edit_phone.text.toString().trim())
                            requestBodyuser.addProperty("cv", userIamge)
                            requestBodyuser.addProperty(
                                "address_line_1",
                                edit_address.text.toString().trim()
                            )
                            requestBodyuser.addProperty(
                                "address_line_2",
                                edit_landmark.text.toString().trim()
                            )
                            requestBodyuser.addProperty("sector", sector_id)
                            requestBodyuser.addProperty("occupation", occupation_id)
                            requestBodyuser.addProperty("country", country_id)
                            requestBodyuser.addProperty("city", city_id)
                            requestBodyuser.addProperty("state", state_id)
                            requestBodyuser.addProperty("zip", edit_zip.text.toString().trim())
                            requestBodyuser.addProperty(
                                "password",
                                edit_password.text.toString().trim()
                            )
                            requestBodyuser.addProperty(
                                "confirm_password",
                                edit_confrim_pass.text.toString().trim()
                            )
                            requestBodyuser.addProperty("agreeTerms", ischeck)
                            requestBodyuser.addProperty("agreePrivacy", ischeck)

                            userSignup(requestBodyuser)
                        }
                     /*   rl_signup_text_layout.visibility=View.GONE
                        iv_signup_first.visibility=View.GONE
                        rl_signup_upload_dov_layout.visibility=View.VISIBLE*/
                    }

                }

            }
            R.id.rl_upload_image_submit -> {
                rl_signup_text_layout.visibility=View.GONE
                iv_signup_first.visibility=View.GONE
                rl_signup_upload_dov_layout.visibility=View.VISIBLE

                if (checkVAlidation()) {

                    var requestBodyuser = JsonObject()
                    requestBodyuser.addProperty("first_name", edit_full_name.text.toString().trim())
                    requestBodyuser.addProperty("last_name", edit_last_name.text.toString().trim())
                    requestBodyuser.addProperty("email", edit_email.text.toString().trim())
                    requestBodyuser.addProperty("title", edit_title.text.toString().trim())
                    requestBodyuser.addProperty("phone", edit_phone.text.toString().trim())
                    requestBodyuser.addProperty("cv", userIamge)
                    requestBodyuser.addProperty(
                        "address_line_1",
                        edit_address.text.toString().trim()
                    )
                    requestBodyuser.addProperty(
                        "address_line_2",
                        edit_landmark.text.toString().trim()
                    )
                    requestBodyuser.addProperty("sector", sector_id)
                    requestBodyuser.addProperty("occupation", occupation_id)
                    requestBodyuser.addProperty("country", country_id)
                    requestBodyuser.addProperty("city", city_id)
                    requestBodyuser.addProperty("state", state_id)
                    requestBodyuser.addProperty("zip", edit_zip.text.toString().trim())
                    requestBodyuser.addProperty(
                        "password",
                        edit_password.text.toString().trim()
                    )
                    requestBodyuser.addProperty(
                        "confirm_password",
                        edit_confrim_pass.text.toString().trim()
                    )
                    requestBodyuser.addProperty("agreeTerms", ischeck)
                    requestBodyuser.addProperty("agreePrivacy", ischeck)

                    userSignup(requestBodyuser)
                }

            }
            R.id.rl_country -> {
                countryList()
            }
            R.id.tv_already_account -> {
                val i = Intent(this, SignInActivity::class.java)
                startActivity(i)
                finish()
            }
            R.id.tv_orgnization -> {
                title_signup_text.text=getString(R.string.org_signup_title_text)
                company_which_type="company"
                edit_im_num.hint="Account Number"
                tv_orgnization.setBackgroundColor(resources.getColor(R.color.light_blue))
                tv_signup.setBackgroundColor(resources.getColor(R.color.light_grey1))
                tv_orgnization.setTextColor(ContextCompat.getColor(this, R.color.white));
                tv_signup.setTextColor(ContextCompat.getColor(this, R.color.black));
                rl_upload_image.visibility = View.GONE
                rl_edit_im_num.visibility = View.VISIBLE
                rl_sign_up_in_your_role.visibility = View.VISIBLE
                rl_signup_in_noofemployee.visibility = View.VISIBLE
                rl_orgnization_name.visibility = View.VISIBLE
                rl_occupation.visibility = View.GONE
                rl_upload_image.visibility = View.GONE


            }
            R.id.tv_signup -> {
                title_signup_text.text=getString(R.string.attach_cv)
                company_which_type=""
                edit_im_num.hint="Do You have an IM ID"
                tv_orgnization.setBackgroundColor(resources.getColor(R.color.light_grey1))
                tv_signup.setBackgroundColor(resources.getColor(R.color.light_blue))
                tv_orgnization.setTextColor(ContextCompat.getColor(this, R.color.black));
                tv_signup.setTextColor(ContextCompat.getColor(this, R.color.white));
                rl_upload_image.visibility = View.VISIBLE
                rl_edit_im_num.visibility = View.VISIBLE
                rl_sign_up_in_your_role.visibility = View.GONE
                rl_signup_in_noofemployee.visibility = View.GONE
                rl_orgnization_name.visibility = View.GONE
                rl_occupation.visibility = View.VISIBLE
                rl_upload_image.visibility = View.VISIBLE


            }
            R.id.rl_state -> {
                stateList()
            }
            R.id.rl_city -> {
                cityList()
            }
            R.id.rl_sector -> {
                sectorList()
            }
            R.id.rl_occupation -> {
                occupationsList()
            }
            R.id.simpleCheckBox -> {
                ischeck = !ischeck
            }
        }
    }
    private fun userSignup(jsonObject: JsonObject){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)

            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            if(company_which_type.equals("company")){
                call = apiInterface!!.companySignup(jsonObject)
            }else{
                call = apiInterface!!.userSignup(jsonObject)
            }

            // startActivity(Intent(this@CartListActivity, LoginActivity::class.java))
            //  finish()
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    // Log.e("log",response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            val jsonObject = JSONObject(response.body()!!.toString())

                            val message = jsonObject.optString("message")

                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG)
                                .show()
                            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                            finish()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            val data = jsonObjectError.optString("data")
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun checkVAlidation(): Boolean {
        if(company_which_type.equals("company")){
            occupation_id="1"
            if (edit_full_name.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
                return false
            }
            else  if (edit_last_name.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.last_name1), Toast.LENGTH_SHORT).show()
                return false
            }
            else  if (edit_title.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
                return false
            }else  if (edit_phone.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.enter_phone), Toast.LENGTH_SHORT).show()
                return false
            }
            else  if (country_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_country),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            else  if (edit_address.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_address),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else  if (edit_landmark.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.enter_mark), Toast.LENGTH_SHORT).show()
                return false
            }
            else  if (state_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_state),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }/*else  if (city_id.equals("")) {
                Toast.makeText(this@SignUpActivity, getString(R.string.select_city), Toast.LENGTH_SHORT).show()
                return false
            }*/
            else  if (edit_zip.text.toString().trim().length === 0) {
                Toast.makeText(this@SignUpActivity, getString(R.string.enter_zip), Toast.LENGTH_SHORT).show()
                return false
            }
            else if(edit_orgnization_name.text.toString().trim().length === 0){
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_organization),
                    Toast.LENGTH_SHORT
                ).show()
                return false

            }
            else if(text_your_role.equals("0")){
                    Toast.makeText(this@SignUpActivity, getString(R.string.enter_your_role), Toast.LENGTH_SHORT).show()
                    return false
                }  else if(text_no_of_empl.equals("Number Of Employees")){
                    Toast.makeText(this@SignUpActivity, getString(R.string.enter_no_of_employees), Toast.LENGTH_SHORT).show()
                    return false
            }


            else  if (sector_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_sector),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else  if (occupation_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_occupation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else  if (!Utility.isValidEmail(edit_email.text.toString().trim())) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_valid_email),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else  if (edit_password.text.toString().trim().length < 6) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Password should have minimum 6-16 character",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else if (!edit_confrim_pass.text.toString().trim().equals(
                    edit_password.text.toString().trim()
                )) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.password_and_confirm_pass_should_same),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }/*else  if (userIamge.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_image),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (edit_im_num.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_zip), Toast.LENGTH_SHORT).show()
            return false
        }*/
            return true

        }else {


            if (edit_full_name.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.full_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_last_name.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.last_name1),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_title.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_title),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_phone.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_phone),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (country_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_country),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_address.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_address),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_landmark.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_mark),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (state_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_state),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }/* else if (city_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_city),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }*/ else if (edit_zip.text.toString().trim().length === 0) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_zip),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (sector_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_sector),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (occupation_id.equals("")) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.select_occupation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!Utility.isValidEmail(edit_email.text.toString().trim())) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.enter_valid_email),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (edit_password.text.toString().trim().length < 6) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Password should have minimum 7-16 character",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!edit_confrim_pass.text.toString().trim().equals(
                    edit_password.text.toString().trim()
                )
            ) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.password_and_confirm_pass_should_same),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }/*else  if (userIamge.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_image),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (edit_im_num.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_zip), Toast.LENGTH_SHORT).show()
            return false
        }*/
            return true
        }
    }

    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                return true

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                return false
            }
        } else {
            return true
        }
    }
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // if (imageFor == "profile") {
                CropImage.activity().setAutoZoomEnabled(true).setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this!!)
                //   }
            } else {
                //code for deny
            }

        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==101&&resultCode== RESULT_OK) {
            try {


                val filePath = getActivityResult(data)
                tvUploadPdf.text=filePath.name
                uploadUserImageApi(filePath,filePath.name)


                //   getPDFPath(uploadfileuri)

             /*   val FilePath = UriUtils.getPathFromUri(this, uploadfileuri)
                print("Path  = $FilePath")
                val extension = FilePath.substring(FilePath.lastIndexOf("."))
                if (extension == ".pdf") {
                    val filename = FilePath.substring(FilePath.lastIndexOf("/") + 1)
                    val file: File = File(FilePath)
                    uploadUserImageApi(file)
                }*/

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    getString(R.string.user_can),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        val result = CropImage.getActivityResult(data)
        if (result != null && resultCode == Activity.RESULT_OK) {

            val selectedPath = result.uri.path
            if (selectedPath != null) {

                var imageFile = File(getPath(result.uri)!!)
                iv_profile_pic.setImageURI(result.uri)
                //profile_image=profileImageFile!!.path.toString()
                iv_profile_pic.setImageURI(result.uri)
                uploadUserImageApi(imageFile,imageFile.name)




            }
        }

    }


    @SuppressLint("Range")
    fun getPDFPath(data: Uri?){
        val uriString = data.toString()

        val myFile = File(uriString)
        val path = myFile.absolutePath
        var displayName: String? = null

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                // Setting the PDF to the TextView
                cursor = applicationContext!!.contentResolver.query(data!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                  val  pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                   // pdfTextView.text = pdfName
                    uploadUserImageApi(myFile,pdfName)
                }
            } finally {
                cursor?.close()
            }
          /*  try {
                cursor = this.getContentResolver().query(data!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                  val  pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                    displayName = cursor.toString()

                }
            } finally {
                cursor!!.close()
            }*/
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
    }

    fun getFilePathFromURI(context: Context?, contentUri: Uri?): String? {
        //copy file and send new file path
        val fileName: String = getFileName(contentUri)!!
     /*   val wallpaperDirectory: File = File(
            Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY
        )*/
        val wallpaperDirectory = File(contentUri.toString())
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(wallpaperDirectory.toString() + File.separator + fileName)
            // create folder if not exists
            return copyFile.absolutePath
        }
        return null
    }




    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }


    fun getPath(uri: Uri?): String? {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this!!.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(column_index)
            cursor.close()
            return path
        }
        // this is our fallback here
        return uri.path
    }

    var userIamge=""
    private fun uploadUserImageApi(uri: File?,name:String) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progressDialog = ProgressDialog(this@SignUpActivity)
            progressDialog.setMessage(getString(R.string.please_wait))
            progressDialog.setCancelable(false)
            progressDialog.isIndeterminate = true
            progressDialog.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            val bodyList = ArrayList<MultipartBody.Part>()
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uri!!.path)
            val bodydata = MultipartBody.Part.createFormData("file", uri.name, requestFile)
            bodyList.add(bodydata)
            call = apiInterface!!.uploadSingleFile(bodyList)
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    progressDialog.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            val jsonObject = JSONObject(response.body()!!.toString())
                            userIamge = jsonObject.getJSONObject("data").optString("filename").toString()
                          //  Toast.makeText(this@SignUpActivity, "upload image", Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Rahul",t.toString())
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun countryList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<CountryListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getCountry()
            call!!.enqueue(object : Callback<CountryListResponse> {
                override fun onResponse(
                    call: Call<CountryListResponse>,
                    response: retrofit2.Response<CountryListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            countryList = response.body()!!.data as ArrayList<CountryList>?
                            countryDialog(countryList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<CountryListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun sectorList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<SectorListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getSector()
            call!!.enqueue(object : Callback<SectorListResponse> {
                override fun onResponse(
                    call: Call<SectorListResponse>,
                    response: retrofit2.Response<SectorListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            sectotList = response.body()!!.data as ArrayList<SectorList>?
                            sectorDialog(sectotList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun occupationsList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
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
                            occupationsList = response.body()!!.data as ArrayList<OccupationsList>?
                            occupationDialog(occupationsList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<OccupationResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun stateList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<StateListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getState(country_id!!)
            call!!.enqueue(object : Callback<StateListResponse> {
                override fun onResponse(
                    call: Call<StateListResponse>,
                    response: retrofit2.Response<StateListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            stateList = response.body()!!.data as ArrayList<StateList>?
                            stateDialog(stateList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun cityList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this@SignUpActivity)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<CityListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getCity(state_id!!)
            call!!.enqueue(object : Callback<CityListResponse> {
                override fun onResponse(
                    call: Call<CityListResponse>,
                    response: retrofit2.Response<CityListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            cityList = response.body()!!.data as ArrayList<CityList>?
                            cityDialog(cityList!!)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
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
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<CityListResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun countryDialog(list: ArrayList<CountryList>) {
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
        dialog!!.city_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCountry(newText.toString(),list)
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
       dialog!!.recycler_country_list.layoutManager = manager
        countryListAdapter =  CountryListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = countryListAdapter

        dialog!!.show()
    }
    private fun filterCountry(text: String,list: ArrayList<CountryList>) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<CountryList> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s!!.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = countryListAdapter!!.filterList(filterdNames)

    }
    private fun sectorDialog(list: ArrayList<SectorList>) {
        dialog = android.app.Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
       // dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        dialog!!.city_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSector(newText.toString(),list)
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
           sectorListAdapter =  SectorListAdapter(this@SignUpActivity!!, list, this,this)
        dialog!!.recycler_country_list.adapter = sectorListAdapter

        dialog!!.show()
    }
    private fun filterSector(text: String,list: ArrayList<SectorList>) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<SectorList> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s!!.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = sectorListAdapter!!.filterList(filterdNames)

    }
    private fun occupationDialog(list: ArrayList<OccupationsList>) {
        dialog = android.app.Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
       // dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        dialog!!.city_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterOccupation(newText.toString(),list)
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
           occupationListAdapter =  OccupationListAdapter(this@SignUpActivity!!, list, this,this)
        dialog!!.recycler_country_list.adapter = occupationListAdapter

        dialog!!.show()
    }
    private fun filterOccupation(text: String,list: ArrayList<OccupationsList>) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<OccupationsList> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s!!.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = occupationListAdapter!!.filterList(filterdNames)

    }
    private fun stateDialog(list: ArrayList<StateList>) {
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

        dialog!!.city_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString(),list)
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
           stateListAdapter =  StateListAdapter(this@SignUpActivity!!, list, this,this)
        dialog!!.recycler_country_list.adapter = stateListAdapter

        dialog!!.show()
    }
    private fun filter(text: String,list: ArrayList<StateList>) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<StateList> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s!!.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = stateListAdapter!!.filterList(filterdNames)

    }

    private fun cityDialog(list: ArrayList<CityList>) {
        dialog = android.app.Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
     //   dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        dialog!!.city_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCity(newText.toString(),list)
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
           cityListAdapter =  CityListAdapter(this@SignUpActivity!!, list, this,this)
        dialog!!.recycler_country_list.adapter = cityListAdapter

        dialog!!.show()
    }
    private fun filterCity(text: String,list: ArrayList<CityList>) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<CityList> = ArrayList()
        //countryFilterList1 = data!! as ArrayList<SurveyParticipantsItem>?

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s!!.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = cityListAdapter!!.filterList(filterdNames)

    }
    private fun openImageDocDialog() {
        dialog = android.app.Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_open_image_doc)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
        dialog!!.setCanceledOnTouchOutside(true)

        dialog!!.tv_open_image.setOnClickListener{
            if (checkPermission1()){
                CropImage.activity().setAutoZoomEnabled(true).setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this!!)
                dialog!!.dismiss()
            }else{
                requestPermission()
                dialog!!.dismiss()
            }


             /*   CropImage.activity().setAutoZoomEnabled(true).setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this!!)*/

             }
        dialog!!.tv_open_doc.setOnClickListener{
          //  val mimeTypes = arrayOf("image/*", "application/pdf")
            val  chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            val intent = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(intent, 101)
           dialog!!.dismiss()
             }
        dialog!!.tv_cancel.setOnClickListener{

            dialog!!.dismiss()
             }

        dialog!!.show()
    }

    override fun bookSession(position: Int, data: CountryList) {
        tv_country.text=data.name
        tv_state.text=null
        tv_city.text=null
        country_id=data.id.toString()
        dialog!!.dismiss()
    }


    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode==1){
            sector_id=sectotList!!.get(position).id.toString()
            tv_sector.text=sectotList!!.get(position).text
            dialog!!.dismiss()
        }
        if(requestcode==2){
            occupation_id=occupationsList!!.get(position).id.toString()
            tv_occupation.text=occupationsList!!.get(position).text
            dialog!!.dismiss()
        }
        if(requestcode==3){
            state_id=stateList!!.get(position).id.toString()
            tv_state.text=stateList!!.get(position).text
            tv_city.text=null
            dialog!!.dismiss()
        }
        if(requestcode==4){
            city_id=cityList!!.get(position).id.toString()
            tv_city.text=cityList!!.get(position).text
            dialog!!.dismiss()
        }
    }



    override fun stateset(position: Int, data: StateList) {
        state_id=data.id.toString()
        tv_state.text=data.text
        tv_city.text=null
        dialog!!.dismiss()
    }

    override fun citySet(position: Int, data: CityList) {
        city_id=data.id.toString()
        tv_city.text=data.text
        dialog!!.dismiss()
    }

    override fun sectorSet(position: Int, data: SectorList) {
        sector_id=data.id.toString()
        tv_sector.text=data.text
        dialog!!.dismiss()
    }

    override fun bookSession(position: Int, data: OccupationsList) {
        occupation_id=data.id.toString()
        tv_occupation.text=data.text
        dialog!!.dismiss()
    }
    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, 101)
    }
    private fun checkPermission1(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            false
        } else true
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()

                // main logic
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel("You need to allow access permissions",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermission()
                                }
                            })
                    }
                }
            }
        }
    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
    fun getActivityResult( data: Intent?):File {
        var url:String?=null
        val uri = data?.data
        val filePath = uri?.path!!
        val picturePath: List<String> = filePath.split(":")
        val first = picturePath[0]
        val second = picturePath[1]
        if (first == "/document/raw" || first == "/document/primary") {
            url = "/storage/emulated/0/$second"
        }
        return File(url!!)
    }
    private val PERMISSION_REQUEST_CODE1 = 200
    private fun checkPermissionNew(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
    }


}