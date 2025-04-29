package com.myoutdoor.agent.utils

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.barteksc.pdfviewer.PDFView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.fragment.MapViewFragment
import kotlinx.android.synthetic.main.guest_user_alert_pop_up.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection


abstract class BaseFragment : Fragment() {
    lateinit var progressBarPB: CircularProgressDialog
    var builder: AlertDialog.Builder? = null
    var showDialogBox1: Dialog? = null
    private lateinit var mApp: MyApplication
    private lateinit var mActivity: MainActivity
    abstract fun getLayoutId(): Int
    abstract fun onCreateView()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        view.isClickable = true
        view.setBackgroundColor(resources.getColor(R.color.white))
        progressBarPB = CircularProgressDialog(context)
        showDialogBox1 = Dialog(requireContext())

        return view
    }

    fun fragmentBackPressHandle() {
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                requireActivity().onBackPressed()
                MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateView()
    }

    override fun onResume() {
        super.onResume()

        Log.d("@@@@", "Resume")

    }
/*

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as MainActivity
        mApp = requireNotNull(activity).application as MyApplication

    }

    override fun onStart() {
        super.onStart()
//        mApp.registerNetworkStateListener(this)
    }

    override fun onStop() {
        super.onStop()
//        mApp.unregisterNetworkStateListener(this)
        //  ExoplayerHandler.pauseVideo()
    }
*/


    fun isEmailValid(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isValidPassword(password: String?): Boolean {

        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{8,}" +  // at least 8 characters
                "$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun onPause() {
        super.onPause()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        Log.d("@@@@@@@", "pause")
    }

    fun addNewFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        hideKeyboard(requireActivity())
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = requireActivity().supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.add(container, fragment, tag).commitAllowingStateLoss()

    }

    fun showMapViewFragment(fragment: Fragment) {
//        val mapViewFragment = MapViewFragment()
        hideKeyboard(requireActivity())
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Check if the fragment is already added
        val existingFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container)
        if (existingFragment == null) {
            transaction.add(R.id.searchContainer, fragment)
        } else {
            transaction.replace(R.id.searchContainer, fragment)
        }

        transaction.commit()
    }


    fun replaceFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        hideKeyboard(requireActivity())
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = requireActivity().supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(container, fragment, tag).commit()

    }

    fun showLongToast(message: String) {

        if (!isAdded) {
            return
        }

        Toast.makeText(
            requireContext(), message, Toast.LENGTH_LONG
        ).show()
    }

    fun showShortToast(message: String) {
        if (!isAdded) {
            return
        }
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_SHORT
        ).show()
    }

    fun openChrome(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun showHideProgress(showLoader: Boolean, progressBar: ProgressBar) {
        if (showLoader) {


            progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )


        } else {
            progressBar.visibility = View.GONE
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyBoard() {

        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInputFromWindow(requireView().windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
    }

    fun copyTextToClipboard(txtView: TextView) {

        val textToCopy = txtView.text
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clip)
        showShortToast("Copied!")
    }


    fun checkUserUnauthorized(error: String): Boolean {
        var isNotAuthorized = false

        if (error == "401") {
            isNotAuthorized = true
        }

        return isNotAuthorized
    }


    fun showLogoutAlert() {

//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.item_logout_dialog_box)
//        dialog.show()
//        val window = dialog.window
//        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window.setLayout(
//            ViewGroup.LayoutParams.FILL_PARENT,
//            LinearLayout.LayoutParams.FILL_PARENT
//        )
//        window.setGravity(Gravity.CENTER)
//        dialog.tvLogoutLoginVutton.setOnClickListener {
//            userLogout()
//            dialog.dismiss()
//        }

    }



    fun showDialog(message: String) {
        showDialogBox1!!.setContentView(R.layout.popup_show_response)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvShowResponse: TextView = showDialogBox1!!.findViewById<TextView>(R.id.tvShowResponse)
        tvShowResponse.setText(message)
        showDialogBox1!!.show()
        Handler(Looper.getMainLooper()).postDelayed({
            showDialogBox1!!.cancel()
        }, 2000)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox1!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox1!!.getWindow()!!.setAttributes(lp)
    }

    fun showAlert(text: String) {
        builder = AlertDialog.Builder(requireActivity())
        builder?.setMessage("")
            ?.setCancelable(false)
            ?.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->

                dialog.dismiss()

            })
            ?.setNegativeButton("", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.setTitle(text)
        alert.show()
    }



    fun showInternalServer() {

//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.item_internal_server_layout)
//        dialog.setCancelable(false)
//        dialog.show()
//        val window = dialog.window
//        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window.setLayout(
//            ViewGroup.LayoutParams.FILL_PARENT,
//            LinearLayout.LayoutParams.FILL_PARENT
//        )
//        window.setGravity(Gravity.CENTER)
//        dialog.tvOkButton.setOnClickListener {
//            requireActivity().onBackPressed()
//            dialog.dismiss()
//        }

    }


    fun userLogout() {

//        SessionData.I().clearLocalData()
//        val intent = Intent(requireContext(), RegisterActivity::class.java)
//        SessionData.I().makeIntentClearHistory(intent)
//        startActivity(intent)
//        MainActivity.context.clearTimer()

    }


    fun dateFormat(date_: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateInString = date_
        val formatterOut = SimpleDateFormat("MMMM d, yyyy")
        val date: Date = formatter.parse(dateInString)
        var newdate = getFormattedDate(date)
        Log.e("call", "date format:    " + date)
        Log.e("call", "date format:    " + formatterOut.format(date))
        Log.e("call", "date format:    " + newdate)

        return newdate!!
    }

    fun getFormattedDate(date: Date?): String? {
        val cal = Calendar.getInstance()
        cal.time = date
        //2nd of march 2015
        val day = cal[Calendar.DATE]
        return if (!(day > 10 && day < 19)) when (day % 10) {
            1 -> SimpleDateFormat("MMMM d'st', yyyy").format(date)
            2 -> SimpleDateFormat("MMMM d'nd', yyyy").format(date)
            3 -> SimpleDateFormat("MMMM d'rd', yyyy").format(date)
            else -> SimpleDateFormat("MMMM d'th', yyyy").format(date)
        } else SimpleDateFormat("MMMM d'th', yyyy").format(date)
    }

    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        override fun onPreExecute() {
            super.onPreExecute()
//            LicenceAnotherFragment.licenceAnotherFragment.progressBarPB.show()
        }

        // on below line we are calling our do in background method.
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.

            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful

                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)

                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null;
            }
            // on below line we are returning input stream.
            return inputStream;
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
//            LicenceAnotherFragment.licenceAnotherFragment.progressBarPB.dismiss()
            mypdfView.fromStream(result).load()

        }
    }


    public fun guestAlertMessage() {

        showDialogBox1!!.setContentView(R.layout.guest_user_alert_pop_up)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        showDialogBox1!!.tvGuestLogin.setOnClickListener {
            val intent = Intent(activity, LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }

        showDialogBox1!!.tvCancelGuest.setOnClickListener {
            showDialogBox1!!.dismiss()
        }


        showDialogBox1!!.show()
    }




}