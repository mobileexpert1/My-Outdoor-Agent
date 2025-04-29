package com.myoutdoor.agent

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.CircularProgressDialog
import kotlinx.android.synthetic.main.fragment_list_your_property_new.*
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import kotlinx.android.synthetic.main.popup_show_response.*
import kotlinx.android.synthetic.main.toolbar.*


class ListYourPropertyNewFragment(progressBarPB: CircularProgressDialog) : BaseFragment() {
    var progressBar: CircularProgressDialog

    /*fun AppWebViewClients(progressBar: ProgressBar) {
        this.progressBar = progressBar
        progressBar.visibility = View.VISIBLE
    }*/


    override fun getLayoutId(): Int {
        return R.layout.fragment_list_your_property_new
    }
    init {
        this.progressBar =progressBarPB
    }
    override fun onCreateView() {

       // showLongToast("Redirected to Web Url ")
        tvToolbar.setText("List Your Property")
        ivBackpress.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
            requireActivity().onBackPressed()

        }
        webviewListYourProperty.settings.setJavaScriptEnabled(true)
        webviewListYourProperty.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // do your stuff here

                progressBar.dismiss()

                // progressBarPB.dismiss()
                //progressBarPB.hide()
            }
        })



        /*webviewListYourProperty.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }*/

        webviewListYourProperty.loadUrl("https://demov2.myoutdooragent.com/#/app/listyourproperty?reload=true")



    }


}