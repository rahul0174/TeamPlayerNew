package com.cts.teamplayer.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_webview.*


class WebViewActivity: AppCompatActivity() , View.OnClickListener {
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)


        findId()
    }

    private fun findId() {
        if (intent.getStringExtra("activity")!!.equals("report")){
            url="https://dev.teamplayerhr.com/app-survey-result-team"+"?"+"group_id"+"="+intent.getStringExtra(
                "group_id"
            )+"&"+"user_id"+"="+intent.getStringExtra("user_id")+
                    "&"+"subgroup_id"+"="+intent.getStringExtra("subgroup_id")+"&"+"user_type"+"="+intent.getStringExtra(
                "user_type"
            )+"&"+"token"+"="+TeamPlayerSharedPrefrence.getInstance(this).getAccessToken("")

        }else if(intent.getStringExtra("activity")!!.equals("calulator")){
            url=intent.getStringExtra("url")
        }
        else{
            url=intent.getStringExtra("url")
        }


        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(MyWebClient())
        webview.getSettings().setDomStorageEnabled(true)
   /*     webview.getSettings().setAppCacheEnabled(true)
        webview.getSettings()
            .setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")*/
        webview.getSettings().setDatabaseEnabled(true)
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webview.settings.databasePath = applicationContext.filesDir.absolutePath + "/databases";
        //  binder.webview.setWebChromeClient(new WebChromeClient());
        if (url != null) {
            webview.loadUrl(url!!)
            progressBar.setVisibility(View.VISIBLE)
        }
     /*   https://dev.teamplayerhr.com/app-survey-result-team?group_id=4&user_id=3665&subgroup_id=5&user_type=benchmark&token=Bearer
     */

  //      val myWebView: WebView = findViewById(R.id.webview)
       /* myWebView.settings.javaScriptEnabled = true
        //webView.addJavascriptInterface(WebAppInterface(this), "Android")
        myWebView.webViewClient = WebViewClient()
        myWebView.loadUrl(url)*/
     }

    private inner class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            super.shouldOverrideUrlLoading(view, url)
            view.loadUrl(url)
            progressBar.setVisibility(View.VISIBLE)
            // view.loadUrl(url);
            return true
            // return true;
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.setVisibility(View.GONE)
            //  Log.e("URLs", url);
        }

        /* override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
             super.onPageStarted(view, url, favicon)
             Log.e("URLs", url)
         }*/
        // open profile Activity after done payment
    }

    override fun onClick(v: View?) {

    }

}