package com.cts.teamplayer.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R

class ForgetPasswordActivity: AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        findId()
    }

    private fun findId() {


    }

    override fun onClick(v: View?) {

    }

}
