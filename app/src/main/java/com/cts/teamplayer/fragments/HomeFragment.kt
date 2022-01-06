package com.cts.teamplayer.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cts.teamplayer.R
import com.cts.teamplayer.adapters.GroupJoinListAdapter
import com.cts.teamplayer.models.GroupJoinDataItem
import com.cts.teamplayer.models.GroupJoinResponse
import com.cts.teamplayer.models.QuestionWithAnswerResponse
import com.cts.teamplayer.models.QuestionsItemNew
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.dialog_open_questionnaire.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


class HomeFragment: Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var dialog:Dialog?=null
    var questionList: java.util.ArrayList<QuestionsItemNew>? = null
    var groupJoinDataItem: java.util.ArrayList<GroupJoinDataItem>? = null
    var mediaControls: MediaController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

       v.img_play_video.setOnClickListener(this)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        v.img_play_video.visibility=View.VISIBLE

        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = MediaController(activity!!)
            mediaControls!!.setAnchorView(v.simpleVideoView)
        }
        // set the media controller for video view
        v.simpleVideoView.setMediaController(mediaControls)
      //  v.simpleVideoView.setVideoPath("https://youtube.com/watch?v=1OxRDJe0pFI&feature=share");
        v.simpleVideoView.setVideoPath("https://youtu.be/1OxRDJe0pFI");
      //  v.simpleVideoView.start()

        // implement on completion listener on video view
        v.simpleVideoView.setOnCompletionListener(OnCompletionListener {
            Toast.makeText(
                activity,
                "Thank You...!!!",
                Toast.LENGTH_LONG
            ).show() // display a toast when an video is completed
        })
        v.simpleVideoView.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
            Toast.makeText(
                activity,
                "Oops An Error Occur While Playing Video...!!!",
                Toast.LENGTH_LONG
            ).show() // display a toast when an error is occured while playing an video
            false
        })
        val uri: Uri = Uri.parse("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")

     //   v.simpleVideoView.setVideoURI(uri)

       // questionAnswerList()
     //   groupJoinList()


        val frameVideo =
            "<html><body><br><iframe width=\"300\" height=\"130\" src=\"https://www.youtube.com/embed/1OxRDJe0pFI\" frameborder=\"0\" allowfullscreen></iframe></body></html>"

        v.webView1.setWebChromeClient(object : WebChromeClient() {})
        v.webView1.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

        val webSettings = v.webView1.settings

        webSettings.javaScriptEnabled = true

       v.webView1.loadData(frameVideo, "text/html", "utf-8")
        return v
    }

    override fun onDestroy() {
        super.onDestroy()
        v.simpleVideoView.pause();

    }


    private fun showDialogTeamList() {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_open_questionnaire)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(this.resources.getColor(R.color.full_transparent)))

        dialog!!.window!!.setGravity(Gravity.CENTER)
        val rl_cancel = dialog!!.findViewById(R.id.rl_cancel) as RelativeLayout
        val tv_start_questionnaire = dialog!!.findViewById(R.id.tv_start_questionnaire) as TextView
          val recycler_join_group_list = dialog!!.findViewById(R.id.recycler_join_group_list) as RecyclerView
          val iv_click_group_join_cancel = dialog!!.findViewById(R.id.iv_click_group_join_cancel) as ImageView
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

       dialog!!.recycler_join_group_list.layoutManager = manager
        val   groupListAdapter =  GroupJoinListAdapter(activity!!, groupJoinDataItem, this)
        dialog!!.recycler_join_group_list.adapter = groupListAdapter
        dialog!!.show()
        rl_cancel.setOnClickListener {
         dialog!!.dismiss()
        }
       iv_click_group_join_cancel.setOnClickListener {
            dialog!!.dismiss()
        }



    }
    private fun questionAnswerList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
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
                            for (j in 0..questionList!!.size - 1) {

                                if (response.body()!!.data!!.questions!!.get(j)!!.answerSaved!!.equals(
                                        true
                                    )
                                ) {

                                    /*  if(j==i){
                                          ll_1.visibility=View.VISIBLE
                                          ll_2.visibility=View.GONE
                                      }else{
                                          ll_1.visibility=View.GONE
                                          ll_2.visibility=View.VISIBLE
                                      }*/

                                } else {
                                    showDialogTeamList()

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

                override fun onFailure(call: Call<QuestionWithAnswerResponse>, t: Throwable) {
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

    private fun groupJoinList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<GroupJoinResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getgroupJoinListParameter(mpref!!.getAccessToken("").toString())
            call!!.enqueue(object : Callback<GroupJoinResponse> {
                override fun onResponse(
                    call: Call<GroupJoinResponse>,
                    response: retrofit2.Response<GroupJoinResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            groupJoinDataItem =
                                response.body()!!.data as ArrayList<GroupJoinDataItem>?
                            if (groupJoinDataItem!!.size > 0) {
                                showDialogTeamList()
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

                override fun onFailure(call: Call<GroupJoinResponse>, t: Throwable) {
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
            R.id.img_play_video -> {
                simpleVideoView.start()
                v.img_play_video.visibility = View.GONE
                video_view_img.visibility = View.GONE
            }
        }

    }

    override fun onClickItem(position: Int, requestcode: Int) {

    }


}