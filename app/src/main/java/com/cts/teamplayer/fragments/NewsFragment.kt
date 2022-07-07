package com.cts.teamplayer.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cts.teamplayer.R
import com.cts.teamplayer.models.NewsListNewResponse
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_participants_profile.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class NewsFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_news, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        faqDetailByToken(mpref!!.getAccessToken("").toString())
        return v
    }

    override fun onClick(v: View?) {
    }
    private fun faqDetailByToken(token: String) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<NewsListNewResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getnewsDetailByToken(token)
            call!!.enqueue(object : Callback<NewsListNewResponse> {
                override fun onResponse(
                    call: Call<NewsListNewResponse>,
                    response: retrofit2.Response<NewsListNewResponse>
                ) {
                    progress.dismiss()
                    Log.e("log",response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {
                        Glide.with(activity!!)
                            .load(MyConstants.USERS_IMAGE +response.body()!!.data!!.get(0)!!.featureImage)
                            .override(60, 60)
                            .fitCenter() // scale to fit entire image within ImageView
                            .into(iv_news);

                        if(response.body()!!.data!!.get(0)!!.content.toString().equals("null")){
                        }else{
                            tv_news.setText(Html.fromHtml(response.body()!!.data!!.get(0)!!.content))

                        }

                        //  et_first_name.text= Editable.Factory.getInstance().newEditable(response.body()!!.metaData!!.firstName)



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

                override fun onFailure(call: Call<NewsListNewResponse>, t: Throwable) {
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