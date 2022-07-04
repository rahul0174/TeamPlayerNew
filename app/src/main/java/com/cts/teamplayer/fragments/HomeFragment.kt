package com.cts.teamplayer.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.PlayVideoOnYoutubePlayer
import com.cts.teamplayer.activities.QuestionnaireCalculatorActivity
import com.cts.teamplayer.activities.RequestDemoActivity
import com.cts.teamplayer.adapters.GroupJoinListAdapter
import com.cts.teamplayer.models.GroupJoinDataItem
import com.cts.teamplayer.models.GroupJoinResponse
import com.cts.teamplayer.models.QuestionWithAnswerResponse
import com.cts.teamplayer.models.QuestionsItemNew
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.android.youtube.player.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dialog_open_questionnaire.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.yotubeplayeractivity.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


class HomeFragment: Fragment(), View.OnClickListener, ItemClickListner, YouTubePlayer.OnInitializedListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var dialog:Dialog?=null
    var questionList: java.util.ArrayList<QuestionsItemNew>? = null
    var groupJoinDataItem: java.util.ArrayList<GroupJoinDataItem>? = null
    var mediaControls: MediaController? = null



    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home, container, false)


      /*  youtubetPlayer.initialize(
            "AIzaSyAF5oDfirRQXvCz_-J_DmaArB77-rNSpEs",
            object : YouTubePlayer.OnInitializedListener {
                // Implement two methods by clicking on red
                // error bulb inside onInitializationSuccess
                // method add the video link or the playlist
                // link that you want to play In here we
                // also handle the play and pause
                // functionality
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youTubePlayer.loadVideo("1OxRDJe0pFI")
                    youTubePlayer.play()
                }

                // Inside onInitializationFailure
                // implement the failure functionality
                // Here we will show toast
                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Toast.makeText(activity, "Video player Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })*/


       v.img_play_video.setOnClickListener(this)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        v.img_play_video.visibility=View.VISIBLE

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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_play_video -> {
                startActivity(Intent(activity, PlayVideoOnYoutubePlayer::class.java))

                /*  simpleVideoView.start()
                  v.img_play_video.visibility = View.GONE
                  video_view_img.visibility = View.GONE*/
            }
        }

    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode== MyConstants.GROUP_JOIN_REQUEST_CODE){
            val i = Intent(activity, QuestionnaireCalculatorActivity::class.java)
            mpref!!.setEmail(groupJoinDataItem!!.get(position).id.toString())
            startActivity(i)

        }
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        youTubePlayer!!.loadVideo("HzeK7g8cD0Y")
        youTubePlayer.play();
      /*  Toast.makeText(activity, "Initialized Youtube Player successfully", Toast.LENGTH_SHORT).show()

        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)

        if (!wasRestored) {
            youTubePlayer?.cueVideo("1OxRDJe0pFI")
        }*/ }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onPlaying() {
            Toast.makeText(activity, "Good, video is playing ok", Toast.LENGTH_SHORT).show()
        }

        override fun onStopped() {
            Toast.makeText(activity, "Video has stopped", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(activity, "Video has paused", Toast.LENGTH_SHORT).show()
        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(activity, "Click Ad now, make the video creator rich!", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
            Toast.makeText(activity, "Video has started", Toast.LENGTH_SHORT).show()
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            Toast.makeText(activity, "Congratulations! You've completed another video.", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }
    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {

    }

}