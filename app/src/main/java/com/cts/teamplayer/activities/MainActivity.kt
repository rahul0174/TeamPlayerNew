package com.cts.teamplayer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.cts.teamplayer.R
import com.cts.teamplayer.fragments.CalculatorFragment
import com.cts.teamplayer.fragments.FaqsFragment
import com.cts.teamplayer.fragments.HomeFragment
import com.cts.teamplayer.fragments.RequestDemoFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findId()

    }

    private fun findId() {
        img_side_nav.setOnClickListener(this)
        rl_sign_in.setOnClickListener(this)
        rl_sign_up.setOnClickListener(this)
        rl_home.setOnClickListener(this)
        rl_demo.setOnClickListener(this)
        rl_faq.setOnClickListener(this)
        tv_open_home.setOnClickListener(this)
        tv_open_how_work.setOnClickListener(this)
        tv_open_contact_us.setOnClickListener(this)
        tv_open_vision_tech.setOnClickListener(this)
        tv_open_news.setOnClickListener(this)
        tv_open_faq.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_side_nav -> {
                onClickBar()
            } R.id.rl_sign_in -> {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            } R.id.rl_sign_up -> {
            startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
            } R.id.rl_home -> {
            addHomeFragment()
            }R.id.rl_demo -> {
            addReqDemoFragment()
            }R.id.rl_faq -> {
            addFaqFragment()
            }R.id.rl_calcu -> {
            addCalculatorFragment()
            }R.id.tv_open_home -> {
                addHomeFragment()
               }
            R.id.tv_open_how_work -> {

            }R.id.tv_open_contact_us -> {
            addCalculatorFragment()
            }R.id.tv_open_vision_tech -> {
            addCalculatorFragment()
            }R.id.tv_open_news -> {
            addCalculatorFragment()
            }R.id.tv_open_faq -> {
            addCalculatorFragment()
            }
        }
    }
    private fun addHomeFragment() {
      val  homeFragment = HomeFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addReqDemoFragment() {
        val  requestDemoFragment = RequestDemoFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, requestDemoFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addFaqFragment() {
        val  faqFragment = FaqsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, faqFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addCalculatorFragment() {
        val  calculatorFragment = CalculatorFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, calculatorFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

    fun onClickBar() {
        runOnUiThread {
            drawer_layout.openDrawer(GravityCompat.START)
            img_side_nav.setRotation(img_side_nav.getRotation() + 180)
        }
    }
}