package com.myoutdoor.agent.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.maps.model.LatLngBounds
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RetrievePDFFromURL(pdfView: PDFView, private val progressBar: ProgressBar) : AsyncTask<String, Void, InputStream>() {
    private val mypdfView: PDFView = pdfView
    private val progressBarPB: ProgressBar = progressBar

    override fun onPreExecute() {
        super.onPreExecute()
        progressBarPB.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): InputStream? {
        var inputStream: InputStream? = null
        try {
            val url = URL(params.get(0))
            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

            if (urlConnection.responseCode == 200) {
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return inputStream
    }

    override fun onPostExecute(result: InputStream?) {
        mypdfView.fromStream(result).load()
        progressBarPB.visibility = View.GONE
    }
}

fun TextView.setFormattedNumber(number: Double) {
    val formattedNumber = String.format("%.2f", number)
    this.text = formattedNumber
}
fun TextView.setFormattedDollarNumber(number: Double) {
    val formattedNumber = String.format("$%.2f", number)
    this.text = formattedNumber
}


// Extension function to check if two LatLngBounds intersect
fun LatLngBounds.intersects(other: LatLngBounds): Boolean {
    return this.southwest.latitude <= other.northeast.latitude &&
            this.northeast.latitude >= other.southwest.latitude &&
            this.southwest.longitude <= other.northeast.longitude &&
            this.northeast.longitude >= other.southwest.longitude
}

fun Context.isNetworkAvailable(): Boolean {
    try {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getNetworkCapabilities(activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } ?: false
            } else {
                TODO( "VERSION.SDK_INT < M")
            }
        }
    }catch (e:Exception){
        return false
    }
    catch (e: IOException) {
        return false
    }
}

fun Context.ShowMessage(value:String){
    Toast.makeText(this,value, Toast.LENGTH_SHORT).show()
}
