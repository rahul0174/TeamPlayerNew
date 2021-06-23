package com.cts.teamplayer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cts.teamplayer.MainActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() , View.OnClickListener{
    private var mpref: TeamPlayerSharedPrefrence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        mpref = TeamPlayerSharedPrefrence.getInstance(this)


        findId()
     }

    private fun findId() {
        rl_new_user.setOnClickListener(this)
        rl_sign_in.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_new_user -> {
                rl_new_user.setBackgroundResource(R.drawable.rounded_blue_btn)
                rl_sign_in.setBackgroundResource(R.drawable.rounded_corner_blue)
                tv_new_user.setTextColor(ContextCompat.getColor(this, R.color.white));
                tv_signin.setTextColor(ContextCompat.getColor(this, R.color.light_blue));
                startActivity(Intent(this@WelcomeActivity, SignUpActivity::class.java))

            }
            R.id.rl_sign_in -> {
                rl_sign_in.setBackgroundResource(R.drawable.rounded_blue_btn)
                rl_new_user.setBackgroundResource(R.drawable.rounded_corner_blue)
                tv_new_user.setTextColor(ContextCompat.getColor(this, R.color.light_blue));
                tv_signin.setTextColor(ContextCompat.getColor(this, R.color.white));

                startActivity(Intent(this@WelcomeActivity, SignInActivity::class.java))

            }
        }

    }

}