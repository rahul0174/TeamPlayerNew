package com.cts.teamplayer.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
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
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.dialog_country.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class SignUpActivity: AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener,CountryListAdapter.TextBookNow,ItemClickListner {

    private var mpref: TeamPlayerSharedPrefrence? = null
    var countryList: java.util.ArrayList<CountryList>? = null
    var sectotList: java.util.ArrayList<SectorList>? = null
    var occupationsList: java.util.ArrayList<OccupationsList>? = null
    var stateList: java.util.ArrayList<StateList>? = null
    var cityList: java.util.ArrayList<CityList>? = null
    private var dialog: Dialog? = null
    var country_id:String?=""
    var city_id:String?=""
    var state_id:String?=""
    var sector_id:String?=""
    var occupation_id:String?=""
    var text_sector:String?=""
    var text_occupation:String?=""
    var ischeck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mpref = TeamPlayerSharedPrefrence.getInstance(this)

       // uploadImage.setOnClickListener(this)


        findId()
    }

    private fun findId() {
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

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.rl_upload_image -> {

                if (checkPermission())
                    CropImage.activity().setAutoZoomEnabled(true).setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this!!)


/*            val i = Intent(this, UploadIageUserActivity::class.java)
            i.putExtra("userType","user")
            startActivity(i)*/

            }
            R.id.btn_submit -> {

                if (checkVAlidation()) {
                        var requestBodyuser = JsonObject()
                    requestBodyuser.addProperty("first_name", edit_full_name.text.toString().trim())
                    requestBodyuser.addProperty("last_name", edit_full_name.text.toString().trim())
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
            }R.id.tv_orgnization -> {
            tv_orgnization.setBackgroundColor(resources.getColor(R.color.light_blue))
            tv_signup.setBackgroundColor(resources.getColor(R.color.light_grey1))
            tv_orgnization.setTextColor(ContextCompat.getColor(this, R.color.white));
            tv_signup.setTextColor(ContextCompat.getColor(this, R.color.light_blue));


            }R.id.tv_signup -> {
            tv_orgnization.setBackgroundColor(resources.getColor(R.color.light_grey1))
            tv_signup.setBackgroundColor(resources.getColor(R.color.light_blue))
            tv_orgnization.setTextColor(ContextCompat.getColor(this, R.color.light_blue));
            tv_signup.setTextColor(ContextCompat.getColor(this, R.color.white));


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
            }R.id.simpleCheckBox -> {
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
            call = apiInterface!!.userSignup(jsonObject)
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

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if (response.code() == 404) {
                        try {
                            val jsonObject = JSONObject(response.body()!!.toString())

                            val message = jsonObject.optString("message")
                                Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_LONG)
                                    .show()




                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Toast.makeText(this@SignUpActivity, "", Toast.LENGTH_LONG).show()
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
        if (edit_full_name.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
            return false
        }
        else  if (edit_title.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_phone.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_address.text.toString().trim().length === 0) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.enter_address),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (edit_landmark.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_mark), Toast.LENGTH_SHORT).show()
            return false
        }else  if (sector_id.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_sector),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (occupation_id.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_occupation),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (country_id.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_country),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (state_id.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_state),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (city_id.equals("")) {
            Toast.makeText(this@SignUpActivity, getString(R.string.select_city), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_zip.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_zip), Toast.LENGTH_SHORT).show()
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
                "Password should have minimum 7-16 character",
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
        }else  if (userIamge.equals("")) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.select_image),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (edit_im_num.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_zip), Toast.LENGTH_SHORT).show()
            return false
        }

 /*    else  if (country_name!!.equals(null)) {
            Toast.makeText(this@SignUpActivity,getString(R.string.country), Toast.LENGTH_SHORT).show()
            return false
        }
        else  if (et_pass.text.toString().trim().length < 6) {
            Toast.makeText(this@UserSignUpActivity, "Password should have minimum 7-16 character", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (et_confrim_pass.text.toString().trim().length === 0) {
            Toast.makeText(this@UserSignUpActivity, getString(R.string.enter_password1), Toast.LENGTH_SHORT).show()
            return false
        }
        else if (!et_confrim_pass.text.toString().trim().equals(et_pass.text.toString().trim())) {
            Toast.makeText(this@UserSignUpActivity, getString(R.string.password_and_confirm_pass_should_same), Toast.LENGTH_SHORT).show()
            return false
        }*/
        return true
    }

    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                return true

            } else {
                ActivityCompat.requestPermissions(
                    this!!, arrayOf(
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = CropImage.getActivityResult(data)
        if (result != null && resultCode == Activity.RESULT_OK) {

            val selectedPath = result.uri.path
            if (selectedPath != null) {

                var imageFile = File(getPath(result.uri)!!)
                iv_profile_pic.setImageURI(result.uri)
                //profile_image=profileImageFile!!.path.toString()
                    iv_profile_pic.setImageURI(result.uri)
                uploadUserImageApi(imageFile)




            }
        }

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
    private fun uploadUserImageApi(uri: File?) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progressDialog = ProgressDialog(this@SignUpActivity)
            progressDialog.setMessage(getString(R.string.please_wait))
            progressDialog.setCancelable(false)
            progressDialog.isIndeterminate = true
            progressDialog.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            val bodyList = ArrayList<MultipartBody.Part>()
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uri!!)
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
                            userIamge = jsonObject.getJSONObject("data").optString("filename")

                            Log.e("userIamge", userIamge)


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
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
       dialog!!.recycler_country_list.layoutManager = manager
      val   countryListAdapter =  CountryListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = countryListAdapter

        dialog!!.show()
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
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
        val   countryListAdapter =  SectorListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = countryListAdapter

        dialog!!.show()
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
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
        val   countryListAdapter =  OccupationListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = countryListAdapter

        dialog!!.show()
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
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
        val   stateListAdapter =  StateListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = stateListAdapter

        dialog!!.show()
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
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        dialog!!.setCanceledOnTouchOutside(true)

        var manager = LinearLayoutManager(this@SignUpActivity, LinearLayoutManager.VERTICAL, false)
        dialog!!.recycler_country_list.layoutManager = manager
        val   stateListAdapter =  CityListAdapter(this@SignUpActivity!!, list, this)
        dialog!!.recycler_country_list.adapter = stateListAdapter

        dialog!!.show()
    }

    override fun bookSession(position: Int, data: CountryList) {
        tv_country.text=data.name
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
            dialog!!.dismiss()
        }
        if(requestcode==4){
            city_id=cityList!!.get(position).id.toString()
            tv_city.text=cityList!!.get(position).text
            dialog!!.dismiss()
        }
    }

}