package com.cts.teamplayer.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.HistoryAdapter
import com.cts.teamplayer.models.AppQuestionPurchase
import com.cts.teamplayer.models.AppQuestionPurchaseResponse
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class HistoryFragment : Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_history, container, false)

        if (activity!!.equals(null)) {

        } else {
            finid()
        }

        return v
    }

    private fun finid() {
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
       v.tv_ppc_purchase.setOnClickListener(this)
       v.tv_renewal_purchase.setOnClickListener(this)
       v.tv_full_ques_purchase.setOnClickListener(this)
       v.tv_full_app_sub_purchase1.setOnClickListener(this)
       v.tv_sub_purchase.setOnClickListener(this)
       v.tv_app_question_purchase.setOnClickListener(this)
       v.tv_app_question_purchase.setOnClickListener(this)
       v.tv_app_question_purchase_empl.setOnClickListener(this)
        if(mpref!!.getRoal("").equals("2")){
            v.ll_purchase_list.visibility=View.GONE
            v.tv_app_question_purchase_empl.visibility=View.VISIBLE

        }else{
            v.ll_purchase_list.visibility=View.VISIBLE
            v.tv_app_question_purchase_empl.visibility=View.GONE
        }




    }
    var appSubscriptionList: java.util.ArrayList<AppQuestionPurchase>? = null
    private fun appSubscriptionList(s: String) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<AppQuestionPurchaseResponse>? = null//apiInterface.profileImage(body,token);

         if(s.equals("ppcpurchase")){
             call = apiInterface!!.getAppPPCPurchase(mpref!!.getAccessToken(""))
         }else if(s.equals("renewalpurchase")){
             call = apiInterface!!.getRenewalPurchase(mpref!!.getAccessToken(""))

         }else if(s.equals("renewalpurchase")){
             call = apiInterface!!.getRenewalPurchase(mpref!!.getAccessToken(""))

         }else if(s.equals("fullquespurchase")){
             call = apiInterface!!.getfullQuestionnairePurchase(mpref!!.getAccessToken(""))
         }else if(s.equals("subpurchase")){
             call = apiInterface!!.subscriptionPurchase(mpref!!.getAccessToken(""))

         }else if(s.equals("appquestionpurchase")){
             call = apiInterface!!.getAppSubscriptionPurchase(mpref!!.getAccessToken(""))
         }else if(s.equals("appquestionpurchaseempl")){
             call = apiInterface!!.getAppQuestionnairePurchase(mpref!!.getAccessToken(""))

         }else if(s.equals("fullappsubpurchase")){
             call = apiInterface!!.getAppSubscriptionPurchase(mpref!!.getAccessToken(""))

         }


            call!!.enqueue(object : Callback<AppQuestionPurchaseResponse> {
                override fun onResponse(
                    call: Call<AppQuestionPurchaseResponse>,
                    response: retrofit2.Response<AppQuestionPurchaseResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            appSubscriptionList = response.body()!!.data as ArrayList<AppQuestionPurchase>?
                            if(appSubscriptionList!!.size>0){
                                v.ll_purchase_list.visibility=View.GONE
                                v.tv_app_question_purchase_empl.visibility=View.GONE
                                v.iv_no_record.visibility=View.GONE
                                v.recyler_history.visibility=View.VISIBLE
                                setSubscriptionList()
                            }else{
                                v.ll_purchase_list.visibility=View.GONE
                               v.iv_no_record.visibility=View.VISIBLE
                               v.recyler_history.visibility=View.GONE
                               v.tv_app_question_purchase_empl.visibility=View.GONE
                            }




                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
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
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
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

                override fun onFailure(call: Call<AppQuestionPurchaseResponse>, t: Throwable) {
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
    fun setSubscriptionList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        v.recyler_history.layoutManager = manager
        val   groupListAdapter =  HistoryAdapter(activity!!, appSubscriptionList,this)
        v.recyler_history.adapter = groupListAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.iv_minus_reach_out -> {


            } R.id.tv_ppc_purchase -> {
            appSubscriptionList("ppcpurchase")

            } R.id.tv_renewal_purchase -> {
            appSubscriptionList("renewalpurchase")

            } R.id.tv_full_ques_purchase -> {
            appSubscriptionList("fullquespurchase")

            }  R.id.tv_full_app_sub_purchase1 -> {
            appSubscriptionList("fullappsubpurchase")

            } R.id.tv_sub_purchase -> {
            appSubscriptionList("subpurchase")

            } R.id.tv_app_question_purchase -> {
            appSubscriptionList("appquestionpurchase")
            } R.id.tv_app_question_purchase_empl -> {
            appSubscriptionList("appquestionpurchaseempl")
            }
        }
    }

    override fun onClickItem(position: Int, requestcode: Int) {

    }
}