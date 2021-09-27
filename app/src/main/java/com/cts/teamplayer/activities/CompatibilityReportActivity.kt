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
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.CompatibilityReportAdapter
import com.cts.teamplayer.models.CompatibilitySurveyResponse
import com.cts.teamplayer.models.CompatibilitySurveyUserListItem
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_compatibility_report.*
import kotlinx.android.synthetic.main.adapter_group_list_in_team_manage.view.*
import kotlinx.android.synthetic.main.fragment_demo_group.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class CompatibilityReportActivity : AppCompatActivity() , View.OnClickListener, ItemClickListner {
    private var mpref: TeamPlayerSharedPrefrence? = null
    var compatibilityReportUserListItem: java.util.ArrayList<CompatibilitySurveyUserListItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compatibility_report)

        findId()
    }

    private fun findId() {

        mpref = TeamPlayerSharedPrefrence.getInstance(this)
        img_select_name.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.radio_check));
        img_select_id.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.radio_uncheck));
     /*   img_select_name.setBackgroundResource(R.drawable.radio_check);
        img_select_id.setBackgroundResource(R.drawable.radio_uncheck);*/

        img_select_name.setOnClickListener{
            img_select_name.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.radio_check
                )
            );
            img_select_id.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.radio_uncheck
                )
            );

            CompatibilityReportList()


        }
        img_back_on_report.setOnClickListener{
           finish()
        }

        tv_downloadSummary.setOnClickListener{
           /* val intent = Intent(Intent.ACTION_VIEW)
            val url="https://dev.teamplayerhr.com/app-survey-result-team"+"?"+"group_id"+"="+intent.getStringExtra(
                "group_id"
            )+"&"+"user_id"+"="+intent.getStringExtra("group_id")+
                    "&"+"subgroup_id"+"="+intent.getStringExtra("subgroup_id")+"&"+"user_type"+"="+intent.getStringExtra(
                "user_type"
            )+"&"+"token"+"="+TeamPlayerSharedPrefrence.getInstance(this).getAccessToken("")

            intent.data = Uri.parse(url)
            startActivity(intent)*/
           val i = Intent(this, WebViewActivity::class.java).putExtra(
                "group_id", intent.getStringExtra(
                    "group_id"
                )
            )
                .putExtra("user_id", intent.getStringExtra("user_id")).putExtra("subgroup_id", intent.getStringExtra("subgroup_id")
                ).putExtra("user_type", intent.getStringExtra("user_type")).putExtra("activity", "report")
            startActivity(i)


        }
        img_select_id.setOnClickListener{
            img_select_id.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.radio_check));
            img_select_name.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.radio_uncheck
                )
            );
            
            CompatibilityIdReportList()
        }


      /*  {"group_id":"4","user_id":"3664","subgroup_id":"5","user_type":"benchmark"}*/

        var surveyRequest: JsonObject = JsonObject()
        surveyRequest!!.addProperty("group_id", intent.getStringExtra("group_id"))
        surveyRequest!!.addProperty("user_id", intent.getStringExtra("user_id"))
        surveyRequest!!.addProperty("subgroup_id", intent.getStringExtra("subgroup_id"))
        surveyRequest!!.addProperty("user_type", intent.getStringExtra("user_type"))
        CompatibilityReportResulttApi(surveyRequest)
    }

    override fun onClick(v: View?) {

    }
    private fun CompatibilityReportResulttApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progress = ProgressDialog(this)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<CompatibilitySurveyResponse>? = null//apiInterface.profileImage(body,token);

            call = apiInterface!!.compatibilityParameter(mpref!!.getAccessToken(""), jsonObject)
            call!!.enqueue(object : Callback<CompatibilitySurveyResponse> {
                override fun onResponse(
                    call: Call<CompatibilitySurveyResponse>,
                    response: retrofit2.Response<CompatibilitySurveyResponse>
                ) {
                    progress.dismiss()
                    Log.e("log", response.body().toString());
                    if (response.code() >= 200 && response.code() < 210) {

                        try {
                            compatibilityReportUserListItem =
                                response.body()!!.data!!.userList as ArrayList<CompatibilitySurveyUserListItem>?

                            tv_user_name_report.text =
                                "For: " + response.body()!!.data!!.user!!.firstName + " " + response.body()!!.data!!.user!!.lastName
                            tv_name_banchmark.text =
                                "Benchmark Person: " + response.body()!!.data!!.user!!.firstName + " " + response.body()!!.data!!.user!!.lastName

                            val date = response.body()!!.data!!.user!!.createdAt
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                            try {
                                val d: Date = dateFormat.parse(date)
                                /*        println("DATE$d")
                                System.out.println("Formated" + dateFormat.format(d))*/
                                tv_date_in_report.text = "Date: " + dateFormat.format(d)
                            } catch (e: java.lang.Exception) {
                                //java.text.ParseException: Unparseable date: Geting error
                                //     println("Excep$e")
                            }
                            CompatibilityReportList()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            this@CompatibilityReportActivity,
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
                            Toast.makeText(
                                this@CompatibilityReportActivity,
                                message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@CompatibilityReportActivity,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<CompatibilitySurveyResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        this@CompatibilityReportActivity,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    fun CompatibilityReportList(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_significiant_list.layoutManager = manager
        val   groupListAdapter =  CompatibilityReportAdapter(
            this,
            compatibilityReportUserListItem,
            this, "name"
        )
        recycler_significiant_list.adapter = groupListAdapter
    }

    fun CompatibilityIdReportList(){
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_significiant_list.layoutManager = manager
        val   groupListAdapter =  CompatibilityReportAdapter(
            this,
            compatibilityReportUserListItem,
            this, "id"
        )
        recycler_significiant_list.adapter = groupListAdapter
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        TODO("Not yet implemented")
    }

}
