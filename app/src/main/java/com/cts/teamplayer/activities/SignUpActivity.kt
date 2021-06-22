package com.cts.teamplayer.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.cts.teamplayer.util.Utility
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

class SignUpActivity: AppCompatActivity() , View.OnClickListener {

    private var mpref: TeamPlayerSharedPrefrence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mpref = TeamPlayerSharedPrefrence.getInstance(this)

       // uploadImage.setOnClickListener(this)
        rl_upload_image.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

        findId()
    }

    private fun findId() {

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
            R.id.btn_submit->{

            }
        }
    }

    private fun checkVAlidation(): Boolean {
        if (edit_full_name.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity, getString(R.string.full_name), Toast.LENGTH_SHORT).show()
            return false
        }
        else  if (edit_title.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity,getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
            return false
        }else  if (edit_phone.text.toString().trim().length === 0) {
            Toast.makeText(this@SignUpActivity,getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
            return false
        }
      /*  else  if (!Utility.isValidEmail(et_em.text.toString().trim())) {
            Toast.makeText(this@SignUpActivity, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show()
            return false
        }*/
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
            if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true

            } else {
                ActivityCompat.requestPermissions(this!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                return false
            }
        } else {
            return true
        }
    }
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
               /* if(user_profile_pic!!.equals("profilePic")){
                    iv_profile_pic.setImageURI(result.uri)
                    uploadLogoApi(imageFile)

                }else if(user_cover_pic!!.equals("coverPic")){
                    iv_cover_pic_user.setImageURI(result.uri)
                    uploadLogoCoverApi(imageFile)
                    //   coverIamge = jsonObject.optString("file")
                }*/


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

}