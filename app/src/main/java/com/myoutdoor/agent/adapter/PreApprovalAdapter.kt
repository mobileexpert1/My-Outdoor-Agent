package com.myoutdoor.agent.adapter

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.PreApprovalRequest.PreApprovalRequestFragment
import com.myoutdoor.agent.models.preapprovalrequest.Data
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.setFormattedNumber
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PreApprovalAdapter(
    private val context: Context,
    private val preApprovalRequestFragment: PreApprovalRequestFragment,
    private var list: ArrayList<Data>,
//   private val numberImage: IntArray
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var llPreApprovalGridItem: LinearLayout
    private lateinit var llCancelRequest: LinearLayout
    private lateinit var llViewDetails: LinearLayout
    private lateinit var llPreApprovalClick: LinearLayout
    private lateinit var iv_ReqstAccpt: ImageView
    private lateinit var tvRequested: TextView
    private lateinit var tvRequestedDate: TextView
    private lateinit var tvAccepted : TextView
    private lateinit var tvDisplayName: TextView
    private lateinit var tvStateNameCountyName: TextView
    private lateinit var tvLicenseStartDateToEndDate: TextView
    private lateinit var tvAcres: TextView
    private lateinit var tvRequestedDateSubmitted: TextView


    var builder: AlertDialog.Builder? = null

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.pre_approval_request_tab_item, null)
        }



        llPreApprovalGridItem = convertView!!.findViewById(R.id.llPreApprovalGridItem)
        llPreApprovalClick = convertView!!.findViewById(R.id.llPreApprovalClick)
        iv_ReqstAccpt = convertView!!.findViewById(R.id.iv_ReqstAccpt)
        tvRequested = convertView!!.findViewById(R.id.tvRequested)
        tvRequestedDate = convertView!!.findViewById(R.id.tvRequestedDate)
        tvAccepted = convertView!!.findViewById(R.id.tvAccepted)
        tvDisplayName = convertView!!.findViewById(R.id.tvDisplayName)
        tvStateNameCountyName = convertView!!.findViewById(R.id.tvStateNameCountyName)
        tvLicenseStartDateToEndDate = convertView!!.findViewById(R.id.tvLicenseStartDateToEndDate)
        tvAcres = convertView!!.findViewById(R.id.tvAcres)
        tvRequestedDateSubmitted = convertView!!.findViewById(R.id.tvRequestedDateSubmitted)
        llCancelRequest = convertView!!.findViewById(R.id.llCancelRequest)
        llViewDetails = convertView!!.findViewById(R.id.llViewDetails)


        llPreApprovalClick.setOnClickListener {

//            preApprovalRequestFragment.lineceNowFragment(list.get(position).publicKey)
            preApprovalRequestFragment.lineceNowFragment(list.get(position).publicKey, position)
        }

        if (list.size==0){
            Toast.makeText(
                context, "No Pre-Approval Requests", Toast.LENGTH_LONG).show()


        }else {


            if (list.get(position).requestStatus == "Accepted") {
                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + list!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(iv_ReqstAccpt)
                tvAccepted.visibility = View.VISIBLE
                tvRequested.visibility = View.GONE
                llCancelRequest.visibility = View.GONE
                llViewDetails.visibility = View.VISIBLE
                llViewDetails.setBackgroundResource(R.drawable.red_button_shape)
                tvDisplayName.setText(list.get(position).displayName)
                tvStateNameCountyName.setText(list.get(position).stateName + "" + list.get(position).countyName)
                var startDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).licenseStartDate)
                var endDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).licenseEndDate)
                tvLicenseStartDateToEndDate.setText(startDate + " to " + endDate)
                tvAcres.setText(list.get(position).acres.toString())
                var submitDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).dateSubmitted)

                tvRequestedDateSubmitted.setText(submitDate)

                llViewDetails.setOnClickListener {

                    preApprovalRequestFragment.lineceNowFragment(list.get(position).publicKey, position)
                }

            }
            else if (list.get(position).requestType == "PreApproval") {
                if (list != null && list[position].imageFilename != null) {
                    val imageUrl = Constants.PROPERTY_IMAGE_URL + list[position].imageFilename
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(iv_ReqstAccpt)
                } else {
                    iv_ReqstAccpt.setImageResource(R.drawable.ic_logo)
                }
                tvAccepted.visibility = View.GONE
                tvRequested.visibility = View.VISIBLE
                llCancelRequest.visibility = View.VISIBLE
                llViewDetails.visibility = View.GONE
                tvDisplayName.setText(list.get(position).displayName)
                tvStateNameCountyName.setText(list.get(position).stateName + "" + list.get(position).countyName)

                var startDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).licenseStartDate)
                var endDate = preApprovalRequestFragment.dateFormat(list.get(position).licenseEndDate)
                tvLicenseStartDateToEndDate.setText(startDate + " to " + endDate)
                tvAcres.setText(list.get(position).acres.toString())

                var submitDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).dateSubmitted)

                tvRequestedDateSubmitted.setText(submitDate)
                llCancelRequest.setOnClickListener {

                    showCancelDialog(position)
                }
            }
            else if (list.get(position).requestType == "PreSale") {
                if (list != null && list[position].imageFilename != null) {
                    val imageUrl = Constants.PROPERTY_IMAGE_URL + list[position].imageFilename
                    Log.d("ImageLoading", "Loading image from URL: $imageUrl")
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(iv_ReqstAccpt)
                } else {
                    iv_ReqstAccpt.setImageResource(R.drawable.ic_logo)
                }

                tvAccepted.visibility = View.GONE
                tvRequested.visibility = View.VISIBLE
                llCancelRequest.visibility = View.GONE
                llViewDetails.visibility = View.VISIBLE
                llViewDetails.setBackgroundResource(R.drawable.green_button_shape)
                tvDisplayName.setText(list.get(position).displayName)
                tvRequested.setText("PRE SALE INVITE")
                tvRequestedDate.setText("Valid Till:")
                tvStateNameCountyName.setText(list.get(position).stateName + "" + list.get(position).countyName)

                var startDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).licenseStartDate)
                var endDate = preApprovalRequestFragment.dateFormat(list.get(position).licenseEndDate)
                tvLicenseStartDateToEndDate.setText(startDate + " to " + endDate)
                tvAcres.setFormattedNumber(list.get(position).acres)

                var submitDate =
                    preApprovalRequestFragment.dateFormat(list.get(position).dateSubmitted)
                tvRequestedDateSubmitted.setText(submitDate)
                llViewDetails.setOnClickListener {

                    preApprovalRequestFragment.lineceNowFragment(list.get(position).preSaleToken, position)
                }
                llCancelRequest.setOnClickListener {

                    showCancelDialog(position)
                }
            }//DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
        }



//        tvDisplayName.setText(list.get(position).displayName)
//        tvStateNameCountyName.setText(list.get(position).stateName+""+list.get(position).countyName)
//        tvLicenseStartDateToEndDate.setText(list.get(position).licenseStartDate+" to "+list.get(position).licenseEndDate)
//        tvAcres.setText(list.get(position).acres.toString())
//        tvRequestedDateSubmitted.setText(list.get(position).dateSubmitted)

//      textView = convertView.findViewById(R.id.textView)
//      imageView.setImageResource(numberImage[position])
//      textView.text = numbersInWords[position]

        return convertView
    }





    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "MMMM dd', 'yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date?
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }




    fun showCancelDialog(position: Int) {
        builder = AlertDialog.Builder(context)
        builder?.setMessage("")
            ?.setCancelable(false)
            ?.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                if (list.size > 0) {
                    preApprovalRequestFragment!!.setItemPosition(position, list.get(position).preApprRequestID)
                    //  preApprovalRequestFragment!!.removeitem(list.get(position).preApprRequestID)
                    dialog.dismiss()
                }
            })
            ?.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.setTitle("Do you want to cancel your request?")
        alert.show()
    }


}