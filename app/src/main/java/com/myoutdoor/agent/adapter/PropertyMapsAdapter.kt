package com.myoutdoor.agent.adapter

import android.app.Dialog
import android.net.Uri
import android.os.AsyncTask
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.FacebookSdk.getCacheDir
import com.github.barteksc.pdfviewer.PDFView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.LicenceAnotherFragment
import com.myoutdoor.agent.models.licensedetails.formylicense.license.MapFile
import com.myoutdoor.agent.utils.Constants
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.coroutineContext


class PropertyMapsAdapter(
//    var modelList: ArrayList<com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.MapFile>,
    var modelList: ArrayList<MapFile>,
    val activity: FragmentActivity?,
): RecyclerView.Adapter<PropertyMapsAdapter.ViewHolder>() {

    var showDialogBox: Dialog? = null

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvMapsListItem: TextView = itemView.findViewById(R.id.tvMapsListItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.property_maps_list_item, parent, false)
        return PropertyMapsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvMapsListItem.setText(modelList.get(position).mapFileName)
        holder.tvMapsListItem.setOnClickListener {
            showPDFPopup(modelList.get(position).mapFileName)
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    private fun showPDFPopup(pdfName:String) {
        showDialogBox = Dialog(activity!!)

        showDialogBox!!.setContentView(R.layout.popup_show_pdf)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)

        val pdfView = showDialogBox!!.findViewById(R.id.pdfView) as PDFView

        var url:String= Constants.AMENITIES_URL+"Assets/MapFiles/"+pdfName

        RetrievePDFFromURL(pdfView).execute(url)

        ivClose.setOnClickListener {
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



    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        override fun onPreExecute() {
            super.onPreExecute()
//            licenceAnotherFragment.progressBarPB.show()
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
            mypdfView.fromStream(result).load()
//            licenceAnotherFragment.progressBarPB.dismiss()

        }
    }



}