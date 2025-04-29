package com.myoutdoor.agent.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.mylicences.MyLicencesFragment
import com.myoutdoor.agent.models.mylicences.activelicences.Model
import com.myoutdoor.agent.models.mylicences.expiredlicences.ExpiredLicencesModel
import com.myoutdoor.agent.models.mylicences.memberoflicences.MemberModel
import com.myoutdoor.agent.models.mylicences.pendinglicences.PendingModel
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class MyLicencesAdapter(
    private val context: Context,
    private var testList: ArrayList<Any>,
//    private var myLicencesList: ArrayList<Model>,
//    private var memberList: ArrayList<MemberModel>,
//    private var pendingList: ArrayList<PendingModel>,
//    private var expiredList: ArrayList<ExpiredLicencesModel>,
    private val myLicencesFragment: MyLicencesFragment,
    val onItemClickListener: OnItemClickListener,
//   private val numberImage: IntArray
    val callback: (String,Any,Int) -> Unit
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var llActiveGridItem: LinearLayout
    private lateinit var llLicenceViewDetails: LinearLayout
    private lateinit var llExpiredLicence: LinearLayout
    private lateinit var tvActiveRLU: TextView
    private lateinit var tvActivePermit: TextView
    private lateinit var tvMembershipPermit: TextView
    private lateinit var tvMembershipRLU: TextView
    private lateinit var tvPendingPermit: TextView
    private lateinit var tvPendingRLU: TextView
    private lateinit var tvExpiredRLU: TextView
    private lateinit var tvExpiredPermit: TextView
    private lateinit var tvExpired: TextView

    private lateinit var tvHeadDisplayName: TextView
    private lateinit var tvStateNameAndcountyName: TextView
    private lateinit var tvStartDateToEndDate: TextView
    private lateinit var tvMAcres: TextView
    private lateinit var ivDataImage: ImageView
    lateinit var pref: SharedPref

    override fun getCount(): Int {
        pref = SharedPref(context)
        var combinedListSize = 0

//        if (pref.getBoolean(Constants.IS_ACTIVE)) {
//            combinedListSize=testList.size
//          //  combinedListSize = myLicencesList.size + memberList.size + pendingList.size
//        }
        /*else if (pref.getBoolean(Constants.IS_MEMBER)) {
         combinedListSize += memberList.size
      } else if (pref.getBoolean(Constants.IS_PENDING)) {
         combinedListSize += pendingList.size
      }*/
//        if (pref.getBoolean(Constants.IS_EXPIRED)) {
//            combinedListSize += expiredList.size
//        } else {
            combinedListSize = testList.size
//            combinedListSize = myLicencesList.size + memberList.size + pendingList.size
//        }
//        return combinedListSize
        return combinedListSize
    }


    /*   override fun getCount(): Int {
          pref= SharedPref(context)

          var combinedlist=myLicencesList.size+memberList.size+pendingList.size
    //      if (pref.getBoolean(Constants.IS_ACTIVE)==true)
          if (pref.getBoolean(Constants.IS_ACTIVE)==true)
          {
    //         return myLicencesList.size
             return combinedlist
          }
          *//*else if (pref.getBoolean(Constants.IS_MEMBER)==true)
      {
         return memberList.size
      }else if (pref.getBoolean(Constants.IS_PENDING)==true)
      {
         return pendingList.size
      }*//*
     *//* else if (pref.getBoolean(Constants.IS_EXPIRED)==true)
      {
         return expiredList.size
      }*//*
      else
      {
         return expiredList.size
      }
     *//* else{
//         return myLicencesList.size
         return myLicencesList.size+memberList.size+pendingList.size
      }*//*
   }*/

 /*   override fun getItem(position: Int): Any? {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }*/

    override fun getItem(position: Int): Any {
        return testList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.my_licences_active_grid_item, null)
        }

        pref = SharedPref(context)
        // Ensure position is within bounds
        if (position >= testList.size) {
            throw IndexOutOfBoundsException("Position $position out of bounds for list of size ${testList.size}")
        }

        var data = testList[position]
        llActiveGridItem = convertView!!.findViewById(R.id.llActiveGridItem)
        llLicenceViewDetails = convertView!!.findViewById(R.id.llLicenceViewDetails)
//        llLicenceViewDetails.setOnClickListener {
//            onItemClickListener.onClick(position)
//        }

        ivDataImage = convertView!!.findViewById(R.id.ivDataImage)
        tvActiveRLU = convertView.findViewById(R.id.tvActiveRLU)
        tvActivePermit = convertView.findViewById(R.id.tvActivePermit)
        tvMembershipPermit = convertView.findViewById(R.id.tvMembershipPermit)
        tvMembershipRLU = convertView.findViewById(R.id.tvMembershipRLU)
        tvPendingPermit = convertView.findViewById(R.id.tvPendingPermit)
        tvPendingRLU = convertView.findViewById(R.id.tvPendingRLU)
        tvExpiredRLU = convertView.findViewById(R.id.tvExpiredRLU)
        tvExpiredPermit = convertView.findViewById(R.id.tvExpiredPermit)
        llExpiredLicence = convertView.findViewById(R.id.llExpiredLicence)
        tvExpired = convertView.findViewById(R.id.tvExpired)


        tvHeadDisplayName = convertView.findViewById(R.id.tvHeadDisplayName)
        tvStateNameAndcountyName = convertView.findViewById(R.id.tvStateNameAndcountyName)
        tvStartDateToEndDate = convertView.findViewById(R.id.tvStartDateToEndDate)
        tvMAcres = convertView.findViewById(R.id.tvMAcres)

        /*
          "productTypeID":2 PERMIT
          "productTypeID":1 RLU
         */

        when (data) {
            is Model ->{
//                if (pref.getBoolean(Constants.IS_ACTIVE) == true){
//                }
                llLicenceViewDetails.setOnClickListener {
//                    callback.invoke("ACTIVE",data.publicKey,position)
                    callback.invoke("ACTIVE",data,position)
                }

                if (data.contractStatus.contains("Active")) {
                    if (data.licenseStatus.contains("Pending")) {
                        if (data.productTypeID == 1) {
                            if (data.activityType.contains("Renewal") && data.paymentType.contains("Mail-in Check")
                            ) {
                                //Pending RLU
                                tvActiveRLU.setText("PENDING RLU")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Accept")
                                llExpiredLicence.visibility = View.VISIBLE
                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(
                                    data.countyName + "," + data.stateName
                                )

                                var startDate =
                                    myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            }
                            else {
                                tvActiveRLU.setText("PENDING RLU")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Processing")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(
                                    data.countyName + "," + data.stateName
                                )

                                var startDate =
                                    myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate =
                                    myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            }

                        } else {
                            if (data.activityType.contains("Renewal") && data.paymentType.contains("Mail-in Check")
                            ) {
                                //Pending RLU
                                tvActiveRLU.setText("PENDING PERMIT")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Accept")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(
                                    data.countyName + "," + data.stateName
                                )

                                var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            } else {
                                tvActiveRLU.setText("PENDING PERMIT")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Processing")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(data.countyName + "," + data.stateName
                                )

                                var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            }
                            //pending Permit

                        }
                    } else {
                        if (data.productTypeID == 1) {
                            // active rlu
                            tvActiveRLU.setText("ACTIVE RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.GONE
                            llLicenceViewDetails.visibility = View.VISIBLE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(data.displayName)
                            tvStateNameAndcountyName.setText(data.countyName + "," + data.stateName)

                            var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                            var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(data.acres.toString())
                        } else {
                            // active permit
                            tvActiveRLU.setText("ACTIVE PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.GONE
                            llLicenceViewDetails.visibility = View.VISIBLE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(data.displayName)
                            tvStateNameAndcountyName.setText(data.countyName + "," + data.stateName)

                            var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                            var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(data.acres.toString())


                        }
                    }
                }
                else {
                    if (data.licenseStatus.contains("Pending")) {
                        if (data.productTypeID == 1) {
                            // pending rlu
                            if (data.activityType.contains("Renewal") && data.paymentType.contains("Mail-in Check")
                            ) {
                                //Pending RLU
                                tvActiveRLU.setText("PENDING RLU")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Accept")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(data.countyName + "," + data.stateName)

                                var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            } else {
                                tvActiveRLU.setText("PENDING RLU")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Processing")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(data.countyName + "," + data.stateName)

                                var startDate = myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate =
                                    myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            }


                        } else {
                            // pending permit
                            if (data.activityType.contains("Renewal") &&
                                data.paymentType.contains("Mail-in Check")
                            ) {
                                //Pending RLU
                                tvActiveRLU.setText("PENDING PERMIT")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Accept")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(
                                    data.countyName + "," + data.stateName
                                )

                                var startDate =
                                    myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate =
                                    myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            } else {
                                tvActiveRLU.setText("PENDING PERMIT")
                                tvActiveRLU.visibility = View.VISIBLE
                                tvActivePermit.visibility = View.GONE
                                tvMembershipPermit.visibility = View.GONE
                                tvMembershipRLU.visibility = View.GONE
                                tvPendingPermit.visibility = View.GONE
                                tvPendingRLU.visibility = View.GONE
                                tvExpiredRLU.visibility = View.GONE
                                tvExpiredPermit.visibility = View.GONE

                                tvExpired.setText("Processing")
                                llExpiredLicence.visibility = View.VISIBLE

                                llLicenceViewDetails.visibility = View.GONE

                                Picasso.get()
                                    .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .into(ivDataImage)

                                tvHeadDisplayName.setText(data.displayName)
                                tvStateNameAndcountyName.setText(
                                    data.countyName + "," + data.stateName
                                )

                                var startDate =
                                    myLicencesFragment.dateFormat(data.licenseStartDate)
                                var endDate =
                                    myLicencesFragment.dateFormat(data.licenseEndDate)
                                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                                tvMAcres.setText(data.acres.toString())
                            }
                        }
                    } else {
                        if (data.productTypeID == 1) {
                            // future rlu
                            tvActiveRLU.setText("FUTURE RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.GONE
                            llLicenceViewDetails.visibility = View.VISIBLE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(data.displayName)
                            tvStateNameAndcountyName.setText(
                                data.countyName + "," + data.stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(data.licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(data.licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(data.acres.toString())

                        }
                        else {
                            // future permit
                            tvActiveRLU.setText("FUTURE PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.GONE
                            llLicenceViewDetails.visibility = View.VISIBLE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(data.displayName)
                            tvStateNameAndcountyName.setText(
                                data.countyName + "," + data.stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(data.licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(data.licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(data.acres.toString())
                        }
                    }
                }
            }

            is MemberModel -> {
                llLicenceViewDetails.setOnClickListener {
//                    callback.invoke("MEMBER",data.publicKey,position)
                    callback.invoke("MEMBER",data,position)
                }
                if (data.productTypeID==1){
                    tvActiveRLU.visibility=View.GONE
                    tvActivePermit.visibility=View.GONE
                    tvMembershipPermit.visibility=View.GONE
                    tvMembershipRLU.visibility=View.VISIBLE
                    tvPendingPermit.visibility=View.GONE
                    tvPendingRLU.visibility=View.GONE
                    tvExpiredRLU.visibility=View.GONE
                    tvExpiredPermit.visibility=View.GONE

                    llExpiredLicence.visibility = View.GONE
                    llLicenceViewDetails.visibility = View.VISIBLE


                    Picasso.get().load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.layer_11)
                        .error(R.drawable.layer_11)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(data.stateName+""+data.countyName)
                    var startDate= myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate+" to "+endDate)
                    tvMAcres.setText(data.acres.toString())
                }
                else if (data.productTypeID==2) {
                    tvActiveRLU.visibility=View.GONE
                    tvActivePermit.visibility=View.GONE
                    tvMembershipPermit.visibility=View.VISIBLE
                    tvMembershipRLU.visibility=View.GONE
                    tvPendingPermit.visibility=View.GONE
                    tvPendingRLU.visibility=View.GONE
                    tvExpiredRLU.visibility=View.GONE
                    tvExpiredPermit.visibility=View.GONE

                    llExpiredLicence.visibility = View.GONE
                    llLicenceViewDetails.visibility = View.VISIBLE

                    Picasso.get().load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.layer_11)
                        .error(R.drawable.layer_11)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(data.stateName+""+data.countyName)
                    var startDate= myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate = myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate+" to "+endDate)
                    tvMAcres.setText(data.acres.toString())
                }


            }

            is PendingModel -> {
                llLicenceViewDetails.setOnClickListener {
//                    callback.invoke("PENDING",data.agreementName,position)
//                    callback.invoke("PENDING",Constants.AMENITIES_URL+data.licenseAgreement,position)
                    callback.invoke("PENDING",data,position)
                }
                if (data.productTypeID == 1) {  // for RLU
                    tvActiveRLU.visibility = View.GONE
                    tvActivePermit.visibility = View.GONE
                    tvMembershipPermit.visibility = View.GONE
                    tvMembershipRLU.visibility = View.GONE
                    tvPendingPermit.visibility = View.GONE
                    tvPendingRLU.visibility = View.VISIBLE
                    tvExpiredRLU.visibility = View.GONE
                    tvExpiredPermit.visibility = View.GONE

                    llExpiredLicence.visibility = View.GONE
                    llLicenceViewDetails.visibility = View.VISIBLE

                    Picasso.get()
                        .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(
                        data.stateName + "" + data.countyName
                    )
                    var startDate =
                        myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate =
                        myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate + " to " + endDate)
                    tvMAcres.setText(data.acres.toString())

                }
                else if (data.productTypeID == 2) {
                    tvActiveRLU.visibility = View.GONE
                    tvActivePermit.visibility = View.GONE
                    tvMembershipPermit.visibility = View.GONE
                    tvMembershipRLU.visibility = View.GONE
                    tvPendingPermit.visibility = View.VISIBLE
                    tvPendingRLU.visibility = View.GONE
                    tvExpiredRLU.visibility = View.GONE
                    tvExpiredPermit.visibility = View.GONE

                    llExpiredLicence.visibility = View.GONE
                    llLicenceViewDetails.visibility = View.VISIBLE

                    Picasso.get()
                        .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(
                        data.stateName + "" + data.countyName
                    )
                    var startDate =
                        myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate =
                        myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate + " to " + endDate)
                    tvMAcres.setText(data.acres.toString())

                }
            }

            is ExpiredLicencesModel -> {
//                if (pref.getBoolean(Constants.IS_EXPIRED) == true) {
//                }
                if (data.productTypeID == 1) {  // for RLU
                    tvActiveRLU.visibility = View.GONE
                    tvActivePermit.visibility = View.GONE
                    tvMembershipPermit.visibility = View.GONE
                    tvMembershipRLU.visibility = View.GONE
                    tvPendingPermit.visibility = View.GONE
                    tvPendingRLU.visibility = View.GONE
                    tvExpiredRLU.visibility = View.VISIBLE
                    tvExpiredPermit.visibility = View.GONE

                    llExpiredLicence.visibility = View.VISIBLE
                    llLicenceViewDetails.visibility = View.GONE

                    Picasso.get()
                        .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(
                        data.stateName + "" + data.countyName
                    )
                    var startDate =
                        myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate =
                        myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate + " to " + endDate)
                    tvMAcres.setText(data.acres.toString())
                }
                else if (data.productTypeID == 2) {  //
                    tvActiveRLU.visibility = View.GONE
                    tvActivePermit.visibility = View.GONE
                    tvMembershipPermit.visibility = View.GONE
                    tvMembershipRLU.visibility = View.GONE
                    tvPendingPermit.visibility = View.GONE
                    tvPendingRLU.visibility = View.GONE
                    tvExpiredRLU.visibility = View.GONE
                    tvExpiredPermit.visibility = View.VISIBLE

                    llExpiredLicence.visibility = View.VISIBLE
                    llLicenceViewDetails.visibility = View.GONE

                    Picasso.get()
                        .load(Constants.PROPERTY_IMAGE_URL + data.imageFilename)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(ivDataImage)
                    tvHeadDisplayName.setText(data.displayName)
                    tvStateNameAndcountyName.setText(
                        data.stateName + "" + data.countyName
                    )
                    var startDate =
                        myLicencesFragment.dateFormat(data.licenseStartDate)
                    var endDate =
                        myLicencesFragment.dateFormat(data.licenseEndDate)
                    tvStartDateToEndDate.setText(startDate + " to " + endDate)
                    tvMAcres.setText(data.acres.toString())
                }


            }

        }



 /*
*
*
*
*
*
*





        if (pref.getBoolean(Constants.IS_ACTIVE) == true) {

            if (myLicencesList.get(position).contractStatus.contains("Active")) {
                if (myLicencesList.get(position).licenseStatus.contains("Pending")) {
                    if (myLicencesList.get(position).productTypeID == 1) {
                        if (myLicencesList.get(position).activityType.contains("Renewal") && myLicencesList.get(
                                position
                            ).paymentType.contains("Mail-in Check")
                        ) {
                            //Pending RLU
                            tvActiveRLU.setText("PENDING RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Accept")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        } else {
                            tvActiveRLU.setText("PENDING RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        }

                    } else {
                        if (myLicencesList.get(position).activityType.contains("Renewal") && myLicencesList.get(
                                position
                            ).paymentType.contains("Mail-in Check")
                        ) {
                            //Pending RLU
                            tvActiveRLU.setText("PENDING PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Accept")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        } else {
                            tvActiveRLU.setText("PENDING PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        }
                        //pending Permit

                    }
                } else {
                    if (myLicencesList.get(position).productTypeID == 1) {
                        // active rlu
                        tvActiveRLU.setText("ACTIVE RLU")
                        tvActiveRLU.visibility = View.VISIBLE
                        tvActivePermit.visibility = View.GONE
                        tvMembershipPermit.visibility = View.GONE
                        tvMembershipRLU.visibility = View.GONE
                        tvPendingPermit.visibility = View.GONE
                        tvPendingRLU.visibility = View.GONE
                        tvExpiredRLU.visibility = View.GONE
                        tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                        llExpiredLicence.visibility = View.GONE
                        llLicenceViewDetails.visibility = View.VISIBLE

                        Picasso.get()
                            .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(ivDataImage)

                        tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                        tvStateNameAndcountyName.setText(
                            myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                position
                            ).stateName
                        )

                        var startDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                        var endDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                        tvStartDateToEndDate.setText(startDate + " to " + endDate)
                        tvMAcres.setText(myLicencesList.get(position).acres.toString())
                    } else {
                        // active permit
                        tvActiveRLU.setText("ACTIVE PERMIT")
                        tvActiveRLU.visibility = View.VISIBLE
                        tvActivePermit.visibility = View.GONE
                        tvMembershipPermit.visibility = View.GONE
                        tvMembershipRLU.visibility = View.GONE
                        tvPendingPermit.visibility = View.GONE
                        tvPendingRLU.visibility = View.GONE
                        tvExpiredRLU.visibility = View.GONE
                        tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                        llExpiredLicence.visibility = View.GONE
                        llLicenceViewDetails.visibility = View.VISIBLE

                        Picasso.get()
                            .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(ivDataImage)

                        tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                        tvStateNameAndcountyName.setText(
                            myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                position
                            ).stateName
                        )

                        var startDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                        var endDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                        tvStartDateToEndDate.setText(startDate + " to " + endDate)
                        tvMAcres.setText(myLicencesList.get(position).acres.toString())


                    }
                }
            } else {
                if (myLicencesList.get(position).licenseStatus.contains("Pending")) {
                    if (myLicencesList.get(position).productTypeID == 1) {
                        // pending rlu
                        if (myLicencesList.get(position).activityType.contains("Renewal") && myLicencesList.get(
                                position
                            ).paymentType.contains("Mail-in Check")
                        ) {
                            //Pending RLU
                            tvActiveRLU.setText("PENDING RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Accept")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        } else {
                            tvActiveRLU.setText("PENDING RLU")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        }


                    } else {
                        // pending permit
                        if (myLicencesList.get(position).activityType.contains("Renewal") && myLicencesList.get(
                                position
                            ).paymentType.contains("Mail-in Check")
                        ) {
                            //Pending RLU
                            tvActiveRLU.setText("PENDING PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Accept")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        } else {
                            tvActiveRLU.setText("PENDING PERMIT")
                            tvActiveRLU.visibility = View.VISIBLE
                            tvActivePermit.visibility = View.GONE
                            tvMembershipPermit.visibility = View.GONE
                            tvMembershipRLU.visibility = View.GONE
                            tvPendingPermit.visibility = View.GONE
                            tvPendingRLU.visibility = View.GONE
                            tvExpiredRLU.visibility = View.GONE
                            tvExpiredPermit.visibility = View.GONE

                            tvExpired.setText("Processing")
                            llExpiredLicence.visibility = View.VISIBLE

                            llLicenceViewDetails.visibility = View.GONE

                            Picasso.get()
                                .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .into(ivDataImage)

                            tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                            tvStateNameAndcountyName.setText(
                                myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                    position
                                ).stateName
                            )

                            var startDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                            var endDate =
                                myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                            tvStartDateToEndDate.setText(startDate + " to " + endDate)
                            tvMAcres.setText(myLicencesList.get(position).acres.toString())
                        }
                    }
                } else {
                    if (myLicencesList.get(position).productTypeID == 1) {
                        // future rlu
                        tvActiveRLU.setText("FUTURE RLU")
                        tvActiveRLU.visibility = View.VISIBLE
                        tvActivePermit.visibility = View.GONE
                        tvMembershipPermit.visibility = View.GONE
                        tvMembershipRLU.visibility = View.GONE
                        tvPendingPermit.visibility = View.GONE
                        tvPendingRLU.visibility = View.GONE
                        tvExpiredRLU.visibility = View.GONE
                        tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                        llExpiredLicence.visibility = View.GONE
                        llLicenceViewDetails.visibility = View.VISIBLE

                        Picasso.get()
                            .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(ivDataImage)

                        tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                        tvStateNameAndcountyName.setText(
                            myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                position
                            ).stateName
                        )

                        var startDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                        var endDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                        tvStartDateToEndDate.setText(startDate + " to " + endDate)
                        tvMAcres.setText(myLicencesList.get(position).acres.toString())

                    }
                    else {
                        // future permit
                        tvActiveRLU.setText("FUTURE PERMIT")
                        tvActiveRLU.visibility = View.VISIBLE
                        tvActivePermit.visibility = View.GONE
                        tvMembershipPermit.visibility = View.GONE
                        tvMembershipRLU.visibility = View.GONE
                        tvPendingPermit.visibility = View.GONE
                        tvPendingRLU.visibility = View.GONE
                        tvExpiredRLU.visibility = View.GONE
                        tvExpiredPermit.visibility = View.GONE

//               tvExpired.setText("Processing")
                        llExpiredLicence.visibility = View.GONE
                        llLicenceViewDetails.visibility = View.VISIBLE

                        Picasso.get()
                            .load(Constants.PROPERTY_IMAGE_URL + myLicencesList!!.get(position).imageFilename)
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(ivDataImage)

                        tvHeadDisplayName.setText(myLicencesList.get(position).displayName)
                        tvStateNameAndcountyName.setText(
                            myLicencesList.get(position).countyName + "," + myLicencesList.get(
                                position
                            ).stateName
                        )

                        var startDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseStartDate)
                        var endDate =
                            myLicencesFragment.dateFormat(myLicencesList.get(position).licenseEndDate)
                        tvStartDateToEndDate.setText(startDate + " to " + endDate)
                        tvMAcres.setText(myLicencesList.get(position).acres.toString())
                    }
                }
            }

        }

        else if (pref.getBoolean(Constants.IS_MEMBER) == true) {

//         if (memberList.get(position).productTypeID==1) {  // for RLU
            tvActiveRLU.visibility = View.GONE
            tvActivePermit.visibility = View.GONE
            tvMembershipPermit.visibility = View.GONE
            tvMembershipRLU.visibility = View.VISIBLE
            tvPendingPermit.visibility = View.GONE
            tvPendingRLU.visibility = View.GONE
            tvExpiredRLU.visibility = View.GONE
            tvExpiredPermit.visibility = View.GONE

            llExpiredLicence.visibility = View.GONE
            llLicenceViewDetails.visibility = View.VISIBLE

            if (memberList.size > 0) {

                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + memberList!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivDataImage)

                tvHeadDisplayName.setText(memberList.get(position).displayName)
                tvStateNameAndcountyName.setText(
                    memberList.get(position).stateName + "" + memberList.get(
                        position
                    ).countyName
                )
                var startDate =
                    myLicencesFragment.dateFormat(memberList.get(position).licenseStartDate)
                var endDate = myLicencesFragment.dateFormat(memberList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                tvMAcres.setText(memberList.get(position).acres.toString())
            }

//         }
             else if (memberList.get(position).productTypeID==2) {
                tvActiveRLU.visibility=View.GONE
                tvActivePermit.visibility=View.GONE
                tvMembershipPermit.visibility=View.VISIBLE
                tvMembershipRLU.visibility=View.GONE
                tvPendingPermit.visibility=View.GONE
                tvPendingRLU.visibility=View.GONE
                tvExpiredRLU.visibility=View.GONE
                tvExpiredPermit.visibility=View.GONE

                llExpiredLicence.visibility = View.GONE
                llLicenceViewDetails.visibility = View.VISIBLE


                Picasso.get().load(Constants.PROPERTY_IMAGE_URL + memberList!!.get(position).imageFilename)
                   .placeholder(R.drawable.layer_11)
                   .error(R.drawable.layer_11)
                   .into(ivDataImage)
                tvHeadDisplayName.setText(memberList.get(position).displayName)
                tvStateNameAndcountyName.setText(memberList.get(position).stateName+""+memberList.get(position).countyName)
                var startDate= myLicencesFragment.dateFormat(memberList.get(position).licenseStartDate)
                var endDate = myLicencesFragment.dateFormat(memberList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate+" to "+endDate)
                tvMAcres.setText(memberList.get(position).acres.toString())
             }


        }

        else if (pref.getBoolean(Constants.IS_PENDING) == true) {

            if (pendingList.get(position).productTypeID == 1) {  // for RLU
                tvActiveRLU.visibility = View.GONE
                tvActivePermit.visibility = View.GONE
                tvMembershipPermit.visibility = View.GONE
                tvMembershipRLU.visibility = View.GONE
                tvPendingPermit.visibility = View.GONE
                tvPendingRLU.visibility = View.VISIBLE
                tvExpiredRLU.visibility = View.GONE
                tvExpiredPermit.visibility = View.GONE

                llExpiredLicence.visibility = View.GONE
                llLicenceViewDetails.visibility = View.VISIBLE

                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + pendingList!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivDataImage)
                tvHeadDisplayName.setText(pendingList.get(position).displayName)
                tvStateNameAndcountyName.setText(
                    pendingList.get(position).stateName + "" + pendingList.get(
                        position
                    ).countyName
                )
                var startDate =
                    myLicencesFragment.dateFormat(pendingList.get(position).licenseStartDate)
                var endDate =
                    myLicencesFragment.dateFormat(pendingList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                tvMAcres.setText(pendingList.get(position).acres.toString())

            } else if (pendingList.get(position).productTypeID == 2) {
                tvActiveRLU.visibility = View.GONE
                tvActivePermit.visibility = View.GONE
                tvMembershipPermit.visibility = View.GONE
                tvMembershipRLU.visibility = View.GONE
                tvPendingPermit.visibility = View.VISIBLE
                tvPendingRLU.visibility = View.GONE
                tvExpiredRLU.visibility = View.GONE
                tvExpiredPermit.visibility = View.GONE

                llExpiredLicence.visibility = View.GONE
                llLicenceViewDetails.visibility = View.VISIBLE

                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + pendingList!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivDataImage)
                tvHeadDisplayName.setText(pendingList.get(position).displayName)
                tvStateNameAndcountyName.setText(
                    pendingList.get(position).stateName + "" + pendingList.get(
                        position
                    ).countyName
                )
                var startDate =
                    myLicencesFragment.dateFormat(pendingList.get(position).licenseStartDate)
                var endDate =
                    myLicencesFragment.dateFormat(pendingList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                tvMAcres.setText(pendingList.get(position).acres.toString())

            }


        }

        else if (pref.getBoolean(Constants.IS_EXPIRED) == true) {

            if (expiredList.get(position).productTypeID == 1) {  // for RLU
                tvActiveRLU.visibility = View.GONE
                tvActivePermit.visibility = View.GONE
                tvMembershipPermit.visibility = View.GONE
                tvMembershipRLU.visibility = View.GONE
                tvPendingPermit.visibility = View.GONE
                tvPendingRLU.visibility = View.GONE
                tvExpiredRLU.visibility = View.VISIBLE
                tvExpiredPermit.visibility = View.GONE

                llExpiredLicence.visibility = View.VISIBLE
                llLicenceViewDetails.visibility = View.GONE


                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + expiredList!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivDataImage)
                tvHeadDisplayName.setText(expiredList.get(position).displayName)
                tvStateNameAndcountyName.setText(
                    expiredList.get(position).stateName + "" + expiredList.get(
                        position
                    ).countyName
                )
                var startDate =
                    myLicencesFragment.dateFormat(expiredList.get(position).licenseStartDate)
                var endDate =
                    myLicencesFragment.dateFormat(expiredList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                tvMAcres.setText(expiredList.get(position).acres.toString())
            }
            else if (expiredList.get(position).productTypeID == 2) {  //
                tvActiveRLU.visibility = View.GONE
                tvActivePermit.visibility = View.GONE
                tvMembershipPermit.visibility = View.GONE
                tvMembershipRLU.visibility = View.GONE
                tvPendingPermit.visibility = View.GONE
                tvPendingRLU.visibility = View.GONE
                tvExpiredRLU.visibility = View.GONE
                tvExpiredPermit.visibility = View.VISIBLE

                llExpiredLicence.visibility = View.VISIBLE
                llLicenceViewDetails.visibility = View.GONE

                Picasso.get()
                    .load(Constants.PROPERTY_IMAGE_URL + expiredList!!.get(position).imageFilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivDataImage)
                tvHeadDisplayName.setText(expiredList.get(position).displayName)
                tvStateNameAndcountyName.setText(
                    expiredList.get(position).stateName + "" + expiredList.get(
                        position
                    ).countyName
                )
                var startDate =
                    myLicencesFragment.dateFormat(expiredList.get(position).licenseStartDate)
                var endDate =
                    myLicencesFragment.dateFormat(expiredList.get(position).licenseEndDate)
                tvStartDateToEndDate.setText(startDate + " to " + endDate)
                tvMAcres.setText(expiredList.get(position).acres.toString())
            }

        }






        */

//    imageView.setImageResource(numberImage[position])
//    textView.text = numbersInWords[position]
        return convertView
    }

    interface OnItemClickListener {
        fun onClick(index: Int)
    }

}