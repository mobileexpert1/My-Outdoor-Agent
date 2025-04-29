package com.myoutdoor.agent.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.search.Model
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.myoutdoor.agent.utils.setFormattedNumber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Currency
import java.util.Date
import java.util.Locale


class NewAdapterOfSearch(val activity: FragmentActivity?,
                         private val context: Context,
//                         private var searchList: MutableList<Model>,
                         private var searchList: ArrayList<com.myoutdoor.agent.models.search.searchV2.Model>,
                         private val searchFragment: SearchFragment,
                         var onItemClickListener: OnItemClickListener
                         ): RecyclerView.Adapter<NewAdapterOfSearch.ViewHolder>() {
    private var onItemClick: NewAdapterOfSearch.OnItemClickListener? = null
    lateinit var pref: SharedPref


    fun setOnItemClick(onItemClick: OnItemClickListener?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_new_items, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data=searchList[position]
        pref= SharedPref(context)
        holder.apply {


            if (searchList != null) {
//            if (searchList.get(position).imagefilename!=null&&searchList.get(position).imagefilename.size>0){
                if (searchList.get(position).imagefilename != null) {

                    Glide.with(ivSeachImage)
                        .load(Constants.PROPERTY_IMAGE_URL + searchList.get(position).imagefilename)
                        .fitCenter()
                        .placeholder(R.drawable.round_logo)
                        .error(R.drawable.round_logo)
                        .centerCrop()
                        .into(ivSeachImage)


                    Log.e(
                        "%%%%",
                        "imagesbackend    " + Constants.PROPERTY_IMAGE_URL + searchList.get(position).imagefilename
                    )
                } else {

                    Glide.with(ivSeachImage)
                        .load(R.drawable.round_logo)
                        .placeholder(R.drawable.round_logo)
                        .error(R.drawable.round_logo)
                        .into(ivSeachImage)

                }
            }

            var isafter = isSaleStartDateTimeGreaterThanCurrent(data.saleStartDateTime, data.currentDateTime)

            if (isafter==true) {
                tvAdventureSeeker.text= "Available On:"
            }

            Glide.with(ivImage)
                .load(Constants.AMENITIES_URL + searchList.get(position).clientLogoPath)
                .fitCenter()
                .placeholder(R.drawable.round_logo)
                .error(R.drawable.round_logo)
                .centerCrop()
                .into(ivImage)

//        holder.tvHplPermitHead.setText(searchList.get(position).rluPropertyModel.displayName.trim())
            tvHplPermitHead.setText(searchList.get(position).displayName.trim())
//        holder.tvHplPermitPrice.setText("$"+","+searchList.get(position).licenseFee.toString())
            tvHplPermitPrice.text = formatCurrency(searchList.get(position).licenseFee)
            tvPermitCountry.setText(
                searchList.get(position).countyName + " County, " + searchList.get(
                    position
                ).state
            )

            var startDate = searchFragment.dateFormat(searchList.get(position).licenseStartDate)
            var endDate = searchFragment.dateFormat(searchList.get(position).licenseEndDate)
            tvLiceseStartEndDate.setText(startDate + " to\n" + endDate)
            tvPermitAcres.setFormattedNumber(searchList.get(position).acres)


            if (searchList != null) {
                if (searchList[position].amenitiyList != null && searchList.get(position).amenitiyList.size > 0) {
                    rvHorizontalList.visibility = View.VISIBLE
                    var seachPermitListAdapter = SearchPermitListAdapter(searchList[position].amenitiyList)
                    val manager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    rvHorizontalList.setLayoutManager(manager)
                    rvHorizontalList.setAdapter(seachPermitListAdapter)
                } else {
                    rvHorizontalList.visibility = View.INVISIBLE
                }
            }

            var salecount =
                searchList.get(position).maxSaleQtyAllowed - searchList.get(position).saleCount
            var available =
                searchList.get(position).maxSaleQtyAllowed - searchList.get(position).saleCount


            if ((searchList.get(position).productTypeID != 1) && (searchList.get(position).activityType != "Day Pass")) {
                if (salecount <= 0) {
                    tvPermitAvailable.setText("Available " + "0")
                } else {
                    tvPermitAvailable.visibility = View.VISIBLE
                    tvPermitAvailable.setText(
                        "Available " + salecount + " of " + searchList.get(
                            position
                        ).maxSaleQtyAllowed
                    )
                }
            } else {
                tvPermitAvailable.visibility = View.INVISIBLE
            }


            /* if (searchList.get(position).status=="Open"){
            if(searchList.get(position).propertyUserStatus!=0){
               holder.tvPreApproval.setText("PRE-APPROVAL REQUIRED")
               holder.tvPreApproval.visibility=View.VISIBLE
            }
            else{
               holder.tvPreApproval.visibility=View.GONE
            }
        }*/

            if (searchList.get(position).hostApprovalRequired == true) {
                tvPreApproval.setText("PRE-APPROVAL REQUIRED")
                tvPreApproval.visibility = View.VISIBLE
            } else {
                tvPreApproval.visibility = View.GONE
            }


            if (searchList.get(position).activityType == "Day Pass") {
                tvAdventureSeeker.text = "Available:"
            } else {
                tvAdventureSeeker.text = context.getString(R.string.property_licence_terms)
            }


            if (searchList.get(position).productTypeID != 1) {
                if (searchList.get(position).saleCount >= searchList.get(position).maxSaleQtyAllowed
                    && searchList.get(position).activityType != "Day Pass"
                ) {
                    tvSoldOut.visibility = View.VISIBLE
                } else {
                    tvSoldOut.visibility = View.GONE
                }
            } else {
                tvSoldOut.visibility = View.GONE

            }

            imvMapDetails.setOnClickListener {
                onItemClickListener!!.onMapClickListener(position)


            }

            clPermitRVItem.setOnClickListener {
                onItemClickListener.onClickListListener(position)
            }

        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun isSaleStartDateTimeGreaterThanCurrent(saleStartDateTime: String, currentDate: String): Boolean {
        // Define the date format
        val formatter = DateTimeFormatter.ISO_DATE_TIME

        // Parse the given date strings
        val saleStart = LocalDateTime.parse(saleStartDateTime, formatter)
        val current = LocalDateTime.parse(currentDate, formatter)

        // Compare the dates
        return saleStart.isAfter(current)
    }

    private fun formatCurrency(amount: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        numberFormat.currency = Currency.getInstance("USD")
        return numberFormat.format(amount)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        
        var  ivSeachImage : ImageView = itemView.findViewById(R.id.ivSeachImage)
        var  ivImage : ImageView = itemView.findViewById(R.id.ivImage)
        var tvHplPermitHead : TextView= itemView.findViewById(R.id.tvHplPermitHead)
        var rvHorizontalList :RecyclerView=itemView.findViewById(R.id.rvHorizontalList)
        var tvHplPermitPrice : TextView=itemView.findViewById(R.id.tvHplPermitPrice)
        var tvPermitCountry :TextView=itemView.findViewById(R.id.tvPermitCountry)
        var tvPermitAvailable :TextView=itemView.findViewById(R.id.tvPermitAvailable)
        var tvLiceseStartEndDate :TextView=itemView.findViewById(R.id.tvLiceseStartEndDate)
        var tvPermitAcres :TextView=itemView.findViewById(R.id.tvPermitAcres)
        var clPermitRVItem : ConstraintLayout = itemView.findViewById(R.id.clPermitRVItem)
        var tvPreApproval :TextView =itemView.findViewById(R.id.tvPreApproval)
        var tvSoldOut :TextView=itemView.findViewById(R.id.tvSoldOut)
        var tvAdventureSeeker :TextView=itemView.findViewById(R.id.tvAdventureSeeker)
        var imvMapDetails :ImageView=itemView.findViewById(R.id.imvMapDetails)
       

    }

    fun loadMore(newList: ArrayList<com.myoutdoor.agent.models.search.searchV2.Model>) {
        Log.e("sdfsdfsf", "newLsit: $newList", )
        if (searchList.size > 0) {
            Log.e("sdfsdfsf", "loadMore: $newList", )
            searchList.addAll(searchList.size, newList)
           notifyItemChanged(searchList.size-1)
        } else {
            searchList.addAll(newList)
            notifyDataSetChanged()
        }

       /* searchList.clear()
        Log.e("sdfsdfsf", "loadMore: $newList", )
        searchList.addAll(newList.distinct())
        notifyDataSetChanged()*/

    }


    interface OnItemClickListener {
        fun onClickListListener(index: Int)

        fun onMapClickListener(index: Int)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
}
