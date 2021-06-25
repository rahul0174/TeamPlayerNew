package com.cts.teamplayer.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.util.Utility
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity: AppCompatActivity() , View.OnClickListener {
    var profession_id:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)


        findId()
    }

    private fun findId() {
        iv_back_profile.setOnClickListener(this)
        iv_profession.setOnClickListener(this)
        btn_save.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back_profile -> {
            finish()
            } R.id.tv_profession_update -> {
            } R.id.btn_save -> {
                if(checkVAlidation()){
                    finish()
                }
            }
        }
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
        }
        else  if (!Utility.isValidEmail(edit_email_update.text.toString().trim())) {
            Toast.makeText(
                this@UpdateProfileActivity,
                getString(R.string.enter_valid_email),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        else  if (edit_address_update.text.toString().trim().length === 0) {
            Toast.makeText(
                this@UpdateProfileActivity,
                getString(R.string.enter_address),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else  if (profession_id.equals("")) {
            Toast.makeText(
                this@UpdateProfileActivity,
                getString(R.string.select_profession),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}

