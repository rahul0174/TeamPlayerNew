package com.cts.teamplayer.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.cts.teamplayer.R
import com.cts.teamplayer.fragments.*
import com.cts.teamplayer.models.AnswersItemNew
import com.cts.teamplayer.models.QuestionWithAnswerResponse
import com.cts.teamplayer.models.QuestionsItemNew
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.fragment_questionnairecalculator.*
import kotlinx.android.synthetic.main.nav_header.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mpref: TeamPlayerSharedPrefrence? = null
    var remaining:Int?=null
    lateinit var homeFragment: androidx.fragment.app.Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findId()

    }

    private fun findId() {
        mpref = TeamPlayerSharedPrefrence.getInstance(this)
        val token = mpref!!.getAccessToken("")
      if(mpref!!.getAccessToken("").equals(""))  {
          ll_main_in_login.visibility=View.GONE
          ll_main.visibility=View.VISIBLE
          addReqDemoFragment()

      }else{
          ll_main_in_login.visibility=View.VISIBLE
          ll_main.visibility=View.GONE
          if(mpref!!.getRoal("").equals("2"))  {

              if(intent.getStringExtra("SPLASH").equals("1")){
                  addHomeFragment()
              }else{
                  if(TeamPlayerSharedPrefrence.getInstance(this).getIsQuestionnaireName("").toString()=="true"){
                      addInviteGroupFragment()

                  }else{
                      addAppQuestionnaireFragment()
                      questionAnswerList()
                  }
                 // if(TeamPlayerSharedPrefrence.getInstance(this).getIsQuestionnaireName(""))
               /*   addAppQuestionnaireFragment()
                  questionAnswerList()*/
              }


          }else{
              if(intent.getStringExtra("SPLASH").equals("1")){
                  addHomeFragment()
              }else{
                  addInviteGroupFragment()
                  questionAnswerList()
              }


          }
        //  addHomeFragment()

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
        tv_puchase_history_frg.setOnClickListener(this)
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
      //  tv_open_news_in_logout.setOnClickListener(this)
        tv_open_news_in_logout.setOnClickListener(this)
        tv_participant_full_questionnaire.setOnClickListener(this)
        tv_participant_app_questionnaire.setOnClickListener(this)
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
            if(mpref!!.getAccessToken("")!!.equals("")){
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }else{
                addInviteGroupFragment()
            }

            }R.id.rl_faq -> {
            if(mpref!!.getAccessToken("")!!.equals("")){
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }else{
                addPurchaseQuesFragment()
            }

                //  addFaqFragment()
            }R.id.rl_calcu -> {
           addAppQuestionnaireFragment()
           // addCompareImIntrinsicFragment()
          //  addCalculatorFragment()
            }/*R.id.rl_calcu -> {
            addCompareImIntrinsicFragment()
          //  addCalculatorFragment()
            }*/R.id.tv_puchase_history_frg -> {
            drawer_layout.closeDrawers()
            addHistoryFragment()
          //  addCalculatorFragment()
            }R.id.tv_participant_app_questionnaire -> {
            drawer_layout.closeDrawers()
            addAppQuestionnaireFragment()
           // addCalculatorFragment()
            //  addCalculatorFragment()
        }
            R.id.tv_participant_full_questionnaire -> {
                drawer_layout.closeDrawers()
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://dev.teamplayerhr.com/purchase"))
                startActivity(i)

         /*   val i = Intent(this, WebViewActivity::class.java)
                .putExtra("activity", "question").putExtra("url","https://dev.teamplayerhr.com/purchase")
*/
           /* val url="https://dev.teamplayerhr.com"
            intent.data = Uri.parse(url)*/

         //   startActivity(i)
          //  addCalculatorFragment()
          //  addCalculatorFragment()
            }R.id.tv_open_home -> {
            img_home.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

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
            drawer_layout.closeDrawers()
            val i = Intent(this, WebViewActivity::class.java)
                .putExtra("activity", "calulator").putExtra("url","https://dev.teamplayerhr.com/mobile-value-calculator")
            startActivity(i)

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
            drawer_layout.closeDrawers()
              addReqDemoFragment()
         //   drawer_layout.closeDrawers()
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
     //   transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addInviteGroupFragment() {

        tv_title_header.text=TeamPlayerSharedPrefrence.getInstance(this).getBusinessName("")
        homeFragment = InviteGroupListFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
     //   transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addPurchaseQuesFragment() {
        tv_title_header.text=getString(R.string.brief_ques_title)
        homeFragment = BriefQuestionnaireFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
       // transaction.addToBackStack(null);
        transaction.commit()

    }private fun addCompareImIntrinsicFragment() {
        tv_title_header.text=getString(R.string.compare_im_title)
        homeFragment = CompareImIntrinsicMatrix()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()

    }private fun addAppQuestionnaireFragment() {
        tv_title_header.text=TeamPlayerSharedPrefrence.getInstance(this).getBusinessName("")
        homeFragment = AppQuestionnaireFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addSubscriptionFragment() {
        tv_title_header.text=getString(R.string.subscription_title)
        homeFragment = SubscriptionFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addHistoryFragment() {
        tv_title_header.text=getString(R.string.purchase_his)
        homeFragment = HistoryFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()

    }
    private fun addHowItFragment() {
        tv_title_header.text=getString(R.string.how_it_works)
        homeFragment = HowItWorksFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
  //      transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun toastMessageUnderDevelopement(){
        Toast.makeText(
            this@MainActivity,
            "This is currently under Development",
            Toast.LENGTH_LONG
        ).show()
    }
    private fun addContactUsFragment() {
        tv_title_header.text=getString(R.string.contact_us)
          homeFragment = ContactUsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()
    } private fun addAboutUsFragment() {
        tv_title_header.text=getString(R.string.about_us)
        homeFragment = AboutUsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
    //    transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addNewsFragment() {
        tv_title_header.text=getString(R.string.news_)
        homeFragment = NewsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
     //   transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addReqDemoFragment() {
        tv_title_header.text=getString(R.string.request_a_demo)
        homeFragment = RequestDemoFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
     //   transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addFaqFragment() {
        tv_title_header.text=getString(R.string.faq)
        homeFragment = FaqsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
     //   transaction.addToBackStack(null);
        transaction.commit()
    }
    private fun addCalculatorFragment() {
        tv_title_header.text=getString(R.string.question_title)
        homeFragment = QuestionnaireCalculator()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
      //  transaction.addToBackStack(null);
        transaction.commit()
    } private fun addParticipantsProfileFragment() {
        tv_title_header.text=getString(R.string.profile)
        homeFragment = ParticipantsProfileFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
    //    transaction.addToBackStack(null);
        transaction.commit()
    }private fun addDemoFragment() {
        tv_title_header.text=getString(R.string.brief_questionnaire)
        homeFragment = DemoFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
     //   transaction.addToBackStack(null);
        transaction.commit()
    }

    fun onClickBar() {
        runOnUiThread {
            drawer_layout.openDrawer(GravityCompat.START)
       //     img_side_nav.setRotation(img_side_nav.getRotation() + 180)
        }
    }
    var questionList: java.util.ArrayList<QuestionsItemNew>? = null
    var answerList: java.util.ArrayList<AnswersItemNew>? = null
    private var selectedList: ArrayList<AnswersItemNew> = ArrayList()
    var question_size:Int?=null
    private fun questionAnswerList(){
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this!!)
            var call: Call<QuestionWithAnswerResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getDemoQuestionWithAnswer(mpref!!.getAccessToken("").toString())
            call!!.enqueue(object : Callback<QuestionWithAnswerResponse> {
                override fun onResponse(
                    call: Call<QuestionWithAnswerResponse>,
                    response: retrofit2.Response<QuestionWithAnswerResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            questionList =
                                response.body()!!.data!!.questions as ArrayList<QuestionsItemNew>?
                            question_size = response.body()!!.data!!.questions!!.size
                            for (j in 0..questionList!!.size-1) {

                                if(response.body()!!.data!!.questions!!.get(j)!!.answerSaved!!.equals(true)){


                                    tv_participant_app_questionnaire.visibility=View.GONE


                                    /*  if(j==i){
                                          ll_1.visibility=View.VISIBLE
                                          ll_2.visibility=View.GONE
                                      }else{
                                          ll_1.visibility=View.GONE
                                          ll_2.visibility=View.VISIBLE
                                      }*/

                                } else{

                                    tv_participant_app_questionnaire.visibility=View.VISIBLE
                                    break
                                }

                            }

                            //   showCounterTimer()


                            /*for (i in 1..questionList!!.size) {
                                answerList =
                                    response.body()!!.data!!.questions!!.get(i)!!.answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)

                            }*/


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@MainActivity,
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
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@MainActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<QuestionWithAnswerResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@MainActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@MainActivity,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (homeFragment is HomeFragment) {
            drawer_layout.closeDrawers()
            // toolbarHeaderLayout.visibility = View.VISIBLE
            finish()

        } else {
            addHomeFragment()
            //super.onBackPressed()

        }
    }
}