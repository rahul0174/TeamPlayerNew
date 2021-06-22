package com.cts.teamplayer.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity: AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        findId()
    }

    private fun findId() {

        btn_login.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                if (edit_user_name.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Please enter username",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (edit_password.text!!.toString().trim { it <= ' ' }.length < 6) {
                    Toast.makeText(
                        this@SignInActivity,
                        resources.getString(R.string.enter_password_length),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                //    loginAPi()
                }
            }
        }
    }
}