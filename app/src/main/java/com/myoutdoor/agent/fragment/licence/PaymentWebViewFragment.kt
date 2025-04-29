package com.myoutdoor.agent.fragment.licence

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.fragment.mylicences.MyLicencesFragment
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import kotlinx.android.synthetic.main.fragment_payment_web_view.*
import kotlinx.android.synthetic.main.toolbar.*


class PaymentWebViewFragment : BaseFragment() {

    var showDialogBox: Dialog? = null
    var paymentToken: String = ""
    var publicKey: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_payment_web_view
    }

    override fun onCreateView() {

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(false /* enabled by default */) {
                override fun handleOnBackPressed() {

                    // Handle the back button even
                    Log.d("BACKBUTTON", "Back button clicks")
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        showDialogBox = Dialog(requireContext())
        MainActivity.mainActivity.bottomNav.visibility = View.GONE
        val bundle = this.arguments
        if (bundle != null) {
            paymentToken = bundle.getString(Constants.PAYMENT_TOKEN)!!
            publicKey = bundle.getString("publickey")!!
        }

        tvToolbar.setText("Payment")

        ivBackpress.setOnClickListener { requireActivity().onBackPressed() }


        setWebView(Constants.TEST_PAYMENT_TOKEN_BASE_URL + paymentToken)

        Log.e("@@@@","PAYMENT_BASE_URL........"+Constants.TEST_PAYMENT_TOKEN_BASE_URL+paymentToken)

    }


    private fun setWebView(url: String) {

        paymentWebView.apply {
            requestFocus()
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true // Allows the website to fit into the screen width
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportZoom(true) // Enable zoom controls
                builtInZoomControls = true
                displayZoomControls = false // Hides the zoom controls for a cleaner UI
            }
            setBackgroundColor(Color.TRANSPARENT)
            setInitialScale(100) // Adjust the scale to ensure a proper fit on mobile devices
            webChromeClient = WebChromeClient()
        }

        paymentWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (it.contains("PaymentStatus=fail")) {
                        responseDialog("Payment Failed", "Unable to process your payment.")
                        return true // Stop loading the URL
                    } else if (it.contains("PaymentStatus=succeeded")) {
                        responseDialog("Payment Completed", "Payment Successful.")
                        return true
                    }
                    view?.loadUrl(it) // Proceed to load the URL
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBarPB.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBarPB.dismiss()
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int, description: String?, failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                showShortToast("Error: $description")
            }
        }

        paymentWebView.loadUrl(url)
    }

    /*
        private fun setWebView(url: String) {

            paymentWebView.requestFocus()
            paymentWebView.settings.javaScriptEnabled = true
            paymentWebView.settings.loadWithOverviewMode = true
    //        documentWebView.settings.useWideViewPort = true
            paymentWebView.settings.domStorageEnabled = true
            paymentWebView.settings.javaScriptCanOpenWindowsAutomatically = true
            paymentWebView.setBackgroundColor(Color.TRANSPARENT)
            paymentWebView.setInitialScale(150)
            paymentWebView.webChromeClient = WebChromeClient()

    //        documentWebView.settings.loadWithOverviewMode = true
    //        documentWebView.settings.useWideViewPort = true

            paymentWebView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                    view!!.loadUrl(url.toString())

                    // cancel url
                    if (url.equals("https://demov2.myoutdooragent.com/#/app/property?id="+publicKey+"&PaymentStatus=fail")) {
                        responseDialog("Payment Failed","Unable to process your payment.")
    //                    requireActivity().onBackPressed()
                    }else if (url!!.contains("PaymentStatus=succeeded")){

                        responseDialog("Payment Completed","Payment Successful.")
                        // success url

                    }else{
                            // error url
                    }
                    Log.d("###@@", url.toString())

                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {

                    progressBarPB.dismiss()

                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressBarPB.show()
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)

                    showShortToast(description.toString())
                }
            }

            paymentWebView.loadUrl(url)

        }
    */

    private fun responseDialog(dailogTitle:String,dialogDiscription:String) {

        showDialogBox!!.setContentView(R.layout.dialog_box_layout)
        showDialogBox!!.setCanceledOnTouchOutside(false)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var dialogTitleName: TextView = showDialogBox!!.findViewById<TextView>(R.id.dialogTitleName)
        var tvDiscription: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvDiscription)
        dialogTitleName.text= dailogTitle
        tvDiscription.text=dialogDiscription
        var okButton: TextView = showDialogBox!!.findViewById<TextView>(R.id.okButton)

        okButton.setOnClickListener {

//            requireActivity().onBackPressed()
            addNewFragment(MyLicencesFragment(),R.id.container,false)
            showDialogBox!!.cancel()
        }
        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)
    }

}