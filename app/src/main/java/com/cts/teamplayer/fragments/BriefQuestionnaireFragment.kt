package com.cts.teamplayer.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.PlanListAdapter
import com.cts.teamplayer.models.*
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants.PAYPAL_CLICK_REQUEST_CODE
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_brief_questionnaire.*
import kotlinx.android.synthetic.main.fragment_brief_questionnaire.view.*
import kotlinx.android.synthetic.main.fragment_invite_group_list.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.*

class BriefQuestionnaireFragment : Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    var planList: java.util.ArrayList<PlanList>? = null
    var REQUEST_CODE = 11

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_brief_questionnaire, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        groupList()
        PerQuestionPrice()
        return v
    }

    override fun onClick(v: View?) {
    }
    private fun groupList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<PlanListResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.plabListParameter(mpref!!.getAccessToken(""))
            call!!.enqueue(object : Callback<PlanListResponse> {
                override fun onResponse(
                    call: Call<PlanListResponse>,
                    response: retrofit2.Response<PlanListResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            planList = response.body()!!.data as ArrayList<PlanList>?

                            setGroupList()

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

                override fun onFailure(call: Call<PlanListResponse>, t: Throwable) {
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
    fun setGroupList(){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        v.recycler_plan_list.layoutManager = manager
        val   groupListAdapter =  PlanListAdapter(activity!!, planList, this)
        v.recycler_plan_list.adapter = groupListAdapter
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode==PAYPAL_CLICK_REQUEST_CODE){
            getbraintreeTokenApi()
        }
    }
    var PerQuestionPriceResponseDataItem: java.util.ArrayList<PerQuestionPriceResponseDataItem>? = null
    private fun PerQuestionPrice(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<PerQuestionPriceResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getDemoPlan()
            call!!.enqueue(object : Callback<PerQuestionPriceResponse> {
                override fun onResponse(
                    call: Call<PerQuestionPriceResponse>,
                    response: retrofit2.Response<PerQuestionPriceResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            PerQuestionPriceResponseDataItem = response.body()!!.data as ArrayList<PerQuestionPriceResponseDataItem>?


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

                override fun onFailure(call: Call<PerQuestionPriceResponse>, t: Throwable) {
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
    var brintreeToken=""
    var orderId=""
    private fun getbraintreeTokenApi() {
     //   Log.e("json",jsonObject.toString())
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>?
            call = apiInterface!!.getclienttokenParameter(
                TeamPlayerSharedPrefrence.getInstance(
                    activity!!
                ).getAccessToken("")
            )
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    Log.e("response111", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            val jsonObject = JSONObject(response.body().toString())
                            /* "{
                             ""orderId"" : ""ORD12345"",
                             ""brainTreeToken"" : ""token123"",
                             ""success"" : true
                         }"*/

                            //    orderId = jsonObject.optString("orderId").toString()
                            brintreeToken = jsonObject.optString("token").toString()

                            val dropInRequest =
                                DropInRequest().clientToken(brintreeToken).collectDeviceData(
                                    true
                                )
                            startActivityForResult(
                                dropInRequest.getIntent(activity!!),
                                REQUEST_CODE
                            )

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }


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
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result: DropInResult? = data!!.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce = result!!.paymentMethodNonce
                val stringNonce = nonce!!.nonce
                /*  "{
                  ""orderId"" : ""ORD12345"",
                  ""amount"" : 450,
                  ""nonceFromTheClient"" : ""nonce123""
              }"*/
               /* {"nonce":"tokencc_bh_q67qb6_ntvz2q_qs9yxp_qnzm3y_k84","chargeAmount":0.99}*/
                val x=v.edit_number_of_participants.text!!.trim()
// Assign two BigDecimal objects

                // Assign two BigDecimal objects
                val b1 = BigDecimal(v.edit_number_of_participants.text!!.trim().toString())
                val b2 = BigDecimal(PerQuestionPriceResponseDataItem!!.get(0).amount)

                // Multiply b1 with b2 and assign result to b3

                // Multiply b1 with b2 and assign result to b3
                val b3: BigDecimal = b1.multiply(b2)
           /*     val b3: BigDecimal = b1.multiply(b2)
                val y = 13
                val z = x * y*/
                val jsonObject = JsonObject()
                jsonObject.addProperty("nonce", stringNonce)
                jsonObject.addProperty(
                    "chargeAmount",
                    b3
                )

                Log.e("json", jsonObject.toString())
                authenticatePaymentApi(jsonObject)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.e("mylog", "user canceled")
            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR)

                Log.e("error", error.toString())

            }
        }

    }
    private fun authenticatePaymentApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(this.resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>?
            call = apiInterface!!.authenticatePayment(
                TeamPlayerSharedPrefrence.getInstance(activity!!).getAccessToken(
                    ""
                ), jsonObject
            )
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        var jsonObject = JSONObject(response.body()!!.toString())
                        try {
                            Log.d("response", response.body()!!.toString())
                            Toast.makeText(
                                activity!!,
                                "Payment done sucessfully",
                                Toast.LENGTH_LONG
                            ).show()
                            //    Toast.makeText(activity!!, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                            /*  var cartTable = dbController!!.getCartInfo("")
                            cartTable.clear()*/

                            /*  startActivity(
                                Intent(activity!!, OrderDoneActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK))*/

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

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
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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



}