package com.cts.teamplayer.activities
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import kotlinx.android.synthetic.main.fragment_home_new.*


class HomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_new)
        initView()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        try {
            val playVideo="<iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"30%\" src=\"https://www.youtube.com/embed/1OxRDJe0pFI\" frameborder=\"0\">"

            webView.clearCache(false)  // <-- DO THIS FIRST

            webView.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE)
            webView.loadData(playVideo, "text/html", "utf-8")
            webView.settings.setJavaScriptEnabled(true)
            webView.settings.setLoadWithOverviewMode(true)
            webView.settings.setUseWideViewPort(true)
            webView.settings.setDomStorageEnabled(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


