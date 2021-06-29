package com.cts.teamplayer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.cts.teamplayer.R
import com.cts.teamplayer.fragments.*
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var homeFragment: androidx.fragment.app.Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findId()

    }

    private fun findId() {
        mpref = TeamPlayerSharedPrefrence.getInstance(this)
      if(mpref!!.getAccessToken("").equals(""))  {
          ll_main_in_login.visibility=View.GONE
          ll_main.visibility=View.VISIBLE
          addReqDemoFragment()

      }else{
          ll_main_in_login.visibility=View.VISIBLE
          ll_main.visibility=View.GONE
          addHomeFragment()
      }
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
        rl_calcu.setOnClickListener(this)

        tv_open_home_in_login.setOnClickListener(this)
        tv_participant_profile_in_login.setOnClickListener(this)
        tv_purchase_Ques_in_login.setOnClickListener(this)
        tv_compare_im_in_login.setOnClickListener(this)
        tv_open_how_work_in_login.setOnClickListener(this)
        tv_benefits_in_login.setOnClickListener(this)
        tv_open_vision_tech_in_login.setOnClickListener(this)
        tv_open_faq_in_login.setOnClickListener(this)
        tv_open_contact_us_in_login.setOnClickListener(this)
        tv_open_subscripation_in_login.setOnClickListener(this)
        tv_demo_in_login.setOnClickListener(this)
        tv_open_news_in_login.setOnClickListener(this)
        tv_open_news_in_logout.setOnClickListener(this)
        tv_open_news_in_logout.setOnClickListener(this)
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
            drawer_layout.closeDrawers()

               }
            R.id.tv_open_how_work -> {
                addHowItFragment()
                drawer_layout.closeDrawers()
            }R.id.tv_open_contact_us -> {
            addContactUsFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_vision_tech -> {
                addAboutUsFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_news -> {
            addNewsFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_faq -> {
            addFaqFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_home_in_login -> {
            addHomeFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_participant_profile_in_login -> {
            addParticipantsProfileFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_participant_profile_in_login -> {
            addParticipantsProfileFragment()
            drawer_layout.closeDrawers()
            }
           R.id.tv_compare_im_in_login -> {
               addCompareImIntrinsicFragment()
               drawer_layout.closeDrawers()

            }R.id.tv_open_how_work_in_login -> {
                addHowItFragment()
            drawer_layout.closeDrawers()

            }R.id.tv_benefits_in_login -> {


            }R.id.tv_open_vision_tech_in_login -> {
                addAboutUsFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_faq_in_login -> {
                addFaqFragment()
            drawer_layout.closeDrawers()

            }R.id.tv_open_contact_us_in_login -> {
                addContactUsFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_subscripation_in_login -> {
            addSubscriptionFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_demo_in_login -> {

            }R.id.tv_open_news_in_login -> {
                addNewsFragment()
            drawer_layout.closeDrawers()

            }R.id.tv_purchase_Ques_in_login -> {
            addPurchaseQuesFragment()
            drawer_layout.closeDrawers()
            }R.id.tv_open_news_in_logout -> {
                mpref!!.clear()
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            finish()


        }
        }
    }
    private fun addHomeFragment() {
        tv_title_header.text=""
        homeFragment = HomeFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addPurchaseQuesFragment() {
        tv_title_header.text=getString(R.string.brief_ques_title)
        homeFragment = BriefQuestionnaireFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()

    }private fun addCompareImIntrinsicFragment() {
        tv_title_header.text=getString(R.string.compare_im_title)
        homeFragment = BriefQuestionnaireFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addSubscriptionFragment() {
        tv_title_header.text=getString(R.string.subscription_title)
        homeFragment = SubscriptionFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addHowItFragment() {
        tv_title_header.text=getString(R.string.how_it_works)
        val  homeFragment = HowItWorksFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addContactUsFragment() {
        tv_title_header.text=getString(R.string.contact_us)
          homeFragment = ContactUsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    } private fun addAboutUsFragment() {
        tv_title_header.text=getString(R.string.about_us)
        val  homeFragment = AboutUsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addNewsFragment() {
        tv_title_header.text=getString(R.string.news_)
        val  homeFragment = NewsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addReqDemoFragment() {
        tv_title_header.text=getString(R.string.request_a_demo)
        val  requestDemoFragment = RequestDemoFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, requestDemoFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addFaqFragment() {
        tv_title_header.text=getString(R.string.faq)
        val  faqFragment = FaqsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, faqFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addCalculatorFragment() {
        tv_title_header.text=getString(R.string.question_title)
        val  calculatorFragment = QuestionnaireCalculator()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, calculatorFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    } private fun addParticipantsProfileFragment() {
        tv_title_header.text=getString(R.string.profile)
        val  participantsProfileFragment = ParticipantsProfileFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, participantsProfileFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

    fun onClickBar() {
        runOnUiThread {
            drawer_layout.openDrawer(GravityCompat.START)
            img_side_nav.setRotation(img_side_nav.getRotation() + 180)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (homeFragment is HomeFragment) {
            // toolbarHeaderLayout.visibility = View.VISIBLE
            finish()
        } else {
            addHomeFragment()
            //super.onBackPressed()

        }
    }
}