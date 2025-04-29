package com.myoutdoor.agent

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.CircularProgressDialog
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import kotlinx.android.synthetic.main.toolbar.*


class PrivacyPolicyFragment(progressBarPB: CircularProgressDialog) : BaseFragment() {
    public lateinit var progressBar: CircularProgressDialog

    /*fun AppWebViewClients(progressBar: ProgressBar) {
        this.progressBar = progressBar
        progressBar.visibility = View.VISIBLE
    }*/


    override fun getLayoutId(): Int {
        return R.layout.fragment_privacy_policy
    }
    init {
        this.progressBar =progressBarPB
    }
    override fun onCreateView() {


        tvToolbar.setText("Privacy Policy")
        ivBackpress.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
            requireActivity().onBackPressed()

        }
        webviewPrivacyPolicy.settings.setJavaScriptEnabled(true)
        webviewPrivacyPolicy.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // do your stuff here
                progressBar.dismiss()
                // progressBarPB.dismiss()
                //progressBarPB.hide()
            }
        })

        /*webviewPrivacyPolicy.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }*/

//        webviewPrivacyPolicy.loadUrl("https://myoutdooragent.com/#/app/policy")
        webviewPrivacyPolicy.loadUrl("https://myoutdooragent.com/policy")



    }


}