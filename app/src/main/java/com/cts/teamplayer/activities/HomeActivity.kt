package com.cts.teamplayer.activities

import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_home_new.*

class HomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_new)

        initView()

    }


    private fun initView() {
        try {
     /*       setSupportActionBar(toolbar)
            toolbarTitleTv.setText(slug);
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)*/
            var playVideo="<iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"30%\" src=\"https://www.youtube.com/embed/1OxRDJe0pFI\" frameborder=\"0\">"
            //var playVideo= "<html><body>Youtube video .. <br> <iframe width="320" height="315" src="https://www.youtube.com/embed/1OxRDJe0pFI" frameborder="0" allowfullscreen></iframe></body></html>";

            //var playVideo= "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"https://www.youtube.com/embed/1OxRDJe0pFI\" frameborder=\"0\"></body></html>"

            // myWebView.loadData(playVideo, "text/html", "utf-8");
            webView.clearCache(false);  // <-- DO THIS FIRST

            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            webView.loadData(playVideo, "text/html", "utf-8");

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);



//            Log.e("url",url)

//            // Initialize Link by loading the Link initiaization URL in the Webview
//            if (url != "")
//                webView.loadUrl(url)
            /* AppDelegate.showProgressDialogCancelable(this@VideoTutorialWebviewActivity)
             webView.webViewClient = object : WebViewClient() {

                 //If you will not use this method url links are opeen in new brower not in webview
                 override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                     var currentUrl: String = url
 //                    Log.e("CurrentURL", currentUrl)
 //                    view.loadUrl(url)

                         var playVideo= "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"https://www.youtube.com/embed/1OxRDJe0pFI\" frameborder=\"0\"></body></html>"

                        // myWebView.loadData(playVideo, "text/html", "utf-8");
                         view.loadData(playVideo, "text/html", "utf-8");
                         return true

                 }


                 override fun onReceivedSslError(
                     view: WebView,
                     handler: SslErrorHandler,
                     error: SslError
                 ) {
                     handler.proceed();
                 }

                 //Show loader on url load
                 override fun onLoadResource(view: WebView, url: String) {
                     //                    progress_bar.visibility= View.VISIBLE


                 }

                 override fun onPageFinished(view: WebView, url: String) {
                     try {
                         //                        progress_bar.visibility= View.GONE
                         AppDelegate.hideProgressDialog(this@VideoTutorialWebviewActivity)
                     } catch (exception: Exception) {
                         exception.printStackTrace()
                     }
                 }

                 override fun onReceivedError(
                     view: WebView,
                     errorCode: Int,
                     description: String,
                     failingUrl: String
                 ) {

                     webView.visibility = View.INVISIBLE
                     txt_error.visibility = View.VISIBLE
                 }
             }*/
            /*val webSettings = webView.getSettings()
            webSettings.setJavaScriptEnabled(true)
            webSettings.setDomStorageEnabled(true)
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE)
            WebView.setWebContentsDebuggingEnabled(true)
            webView.loadUrl(url)*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


