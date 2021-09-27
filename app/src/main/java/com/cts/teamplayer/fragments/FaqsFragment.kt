package com.cts.teamplayer.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cts.teamplayer.R
import com.cts.teamplayer.models.FaqResultResponse
import com.cts.teamplayer.models.UserProfileResponse
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_participants_profile.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FaqsFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var reach_out = 0
    var what_group = 0
    var question_one:String?=null
    var answer_one:String?=null
    var question_two:String?=null
    var answer_two:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_faq, container, false)

        if(activity!!.equals(null)){

        }else{
            finid()
        }

        return v
    }

    private fun finid() {
        v.iv_minus_reach_out.setOnClickListener(this)
        v.iv_plus_what_group.setOnClickListener(this)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        faqDetailByToken(mpref!!.getAccessToken("").toString())

    }
    private fun faqDetailByToken(token: String) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<FaqResultResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getfaqDetailByToken(token)
            call!!.enqueue(object : Callback<FaqResultResponse> {
                override fun onResponse(
                    call: Call<FaqResultResponse>,
                    response: retrofit2.Response<FaqResultResponse>
                ) {
                    progress.dismiss()
                    Log.e("log",response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)

                      question_one  =response.body()!!.data!!.get(0)!!.question
                      answer_one  =response.body()!!.data!!.get(0)!!.answer

                         question_two  =response.body()!!.data!!.get(1)!!.question
                         answer_two  =response.body()!!.data!!.get(1)!!.answer

                        tv_reach_out_ansswer.text=response.body()!!.data!!.get(0)!!.answer
                        tv_what_group_answer.text=response.body()!!.data!!.get(1)!!.answer

                        tv_question_one.text=response.body()!!.data!!.get(0)!!.question
                        tv_question_seond.text=response.body()!!.data!!.get(1)!!.question

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }/*else if(response.code()==401){
                        mpref!!.setToken("")
                        mpref!!.clear()
                        val i = Intent(this@VendorProfileEditActivity, CountryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(i)
                    }*/
                    else {
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
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<FaqResultResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_minus_reach_out -> {
                if(reach_out==0){
                    v.iv_minus_reach_out.setImageResource(R.drawable.plus)
                    iv_plus_what_group.setImageResource(R.drawable.plus)
                  tv_reach_out_ansswer.visibility=View.GONE
                    tv_what_group_answer.visibility=View.GONE

                    reach_out=1
                }else{
                    v.iv_minus_reach_out.setImageResource(R.drawable.minus)
                    iv_plus_what_group.setImageResource(R.drawable.plus)
                    tv_reach_out_ansswer.visibility=View.VISIBLE
                    tv_what_group_answer.visibility=View.GONE
                    reach_out=0

                }

            }
            R.id.iv_plus_what_group -> {
                if(what_group==0){
                   v.iv_plus_what_group.setImageResource(R.drawable.minus)
                    iv_minus_reach_out.setImageResource(R.drawable.plus)
                    what_group=1
                   tv_reach_out_ansswer.visibility=View.GONE
                   tv_what_group_answer.visibility=View.VISIBLE
                }else{
                   v.iv_plus_what_group.setImageResource(R.drawable.plus)
                    iv_minus_reach_out.setImageResource(R.drawable.plus)
                    what_group=0
                    tv_reach_out_ansswer.visibility=View.GONE
                    tv_what_group_answer.visibility=View.GONE

                }

            }
        }
    }

}