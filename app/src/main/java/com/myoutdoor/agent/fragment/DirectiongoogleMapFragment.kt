package com.myoutdoor.agent.fragment

import android.webkit.WebView
import android.webkit.WebViewClient
import com.myoutdoor.agent.R
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.CircularProgressDialog
import kotlinx.android.synthetic.main.fragment_directiongoogle_map.*


class DirectiongoogleMapFragment(progressBarPB: CircularProgressDialog) : BaseFragment() {
    public lateinit var progressBar: CircularProgressDialog


    override fun getLayoutId(): Int {
        return R.layout.fragment_directiongoogle_map
    }
    init {
        this.progressBar = progressBarPB

    }
    override fun onCreateView() {

        webviewDirections.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // do your stuff here
                progressBar.dismiss()
                // progressBarPB.dismiss()
                //progressBarPB.hide()
            }
        })

        webviewDirections.loadUrl("https://www.google.com/maps/")
        webviewDirections.settings.setJavaScriptEnabled(true)


    }


}