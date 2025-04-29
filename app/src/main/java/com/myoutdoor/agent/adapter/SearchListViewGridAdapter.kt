package com.myoutdoor.agent.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.search.Model
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.squareup.picasso.Picasso

class SearchListViewGridAdapter(
    private val context: Context,
//    private var searchList: MutableList<Model>,
    private var searchList: MutableList<com.myoutdoor.agent.models.search.searchV2.Model>,
    private val searchFragment: SearchFragment,
    val onItemClickListener: OnGridItemClickListener) : BaseAdapter()
{
    private var layoutInflater: LayoutInflater? = null
    private lateinit var llPermitGridItem: LinearLayout
    private lateinit var tvHplPermitHead: TextView
    private lateinit var tvHplPermitPrice: TextView
    private lateinit var tvPermitCountry: TextView
    private lateinit var tvPermitAvailable: TextView
    private lateinit var tvLiceseStartEndDate: TextView
    private lateinit var tvPermitAcres: TextView
    private lateinit var tvPreApproval: TextView
    private lateinit var tvSoldOut: TextView
    private lateinit var rvHorizontalList: RecyclerView
    private lateinit var ivSeachImage: ImageView
    lateinit var pref: SharedPref

    override fun getCount(): Int {
            return searchList.size
    }

    override fun getItem(position: Int): Any? {
        return searchList.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.permit_grid_item, null)
        }
        pref= SharedPref(context)
        ivSeachImage = convertView!!.findViewById(R.id.ivSeachImage)
        tvHplPermitHead = convertView.findViewById(R.id.tvHplPermitHead)
        rvHorizontalList = convertView.findViewById(R.id.rvHorizontalList)
        tvHplPermitPrice = convertView.findViewById(R.id.tvHplPermitPrice)
        tvPermitCountry = convertView.findViewById(R.id.tvPermitCountry)
        tvPermitAvailable = convertView.findViewById(R.id.tvPermitAvailable)
        tvLiceseStartEndDate = convertView.findViewById(R.id.tvLiceseStartEndDate)
        tvPermitAcres = convertView.findViewById(R.id.tvPermitAcres)
        llPermitGridItem = convertView.findViewById(R.id.llPermitGridItem)
        tvPreApproval = convertView.findViewById(R.id.tvPreApproval)
        tvSoldOut = convertView.findViewById(R.id.tvSoldOut)

// SearchPermitListAdapter

        Log.e("call","@@@@121222  "+searchList.get(position).amenitiyList.toString())

//        if (searchList.get(position).rluImages.size == 0){
//            Picasso.get().load(R.drawable.ic_logo)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_logo)
//                .into(ivSeachImage)
//        }else {
//            Picasso.get().load(Constants.PROPERTY_IMAGE_URL + searchList.get(position).rluImages.get(0).imageFileName)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_logo)
//                .into(ivSeachImage)
//        }


        if (searchList!=null){
//            if (searchList.get(position).rluImages!=null&&searchList.get(position).rluImages.size>0){
            if (searchList.get(position).imagefilename!=null){
                Picasso.get().load(Constants.PROPERTY_IMAGE_URL + searchList.get(position).imagefilename)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivSeachImage)
            }
            else{
                Picasso.get().load(R.drawable.ic_logo)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivSeachImage)
            }
        }


        tvHplPermitHead.setText(searchList.get(position).displayName)
        tvHplPermitPrice.setText("$"+searchList.get(position).licenseFee.toString())
        tvPermitCountry.setText(searchList.get(position).countyName+" County, "+searchList.get(position).state)

        var startDate= searchFragment.dateFormat(searchList.get(position).licenseStartDate)
        var endDate = searchFragment.dateFormat(searchList.get(position).licenseEndDate)
        tvLiceseStartEndDate.setText(startDate+" to\n"+endDate)
        tvPermitAcres.setText(searchList.get(position).acres.toString())


//        Log.e("call","amenityNameList  "+amenityNameList.toString())
        if (searchList!=null){
//        if (searchList[position].amenityName!=null&&searchList.get(position).amenityName.size>0){
        if (searchList[position].amenitiyList!=null&&searchList.get(position).amenitiyList.size>0){
            rvHorizontalList.visibility=View.VISIBLE
            var seachPermitListAdapter = SearchPermitListAdapter(searchList[position].amenitiyList)
            val manager: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvHorizontalList.setLayoutManager(manager)
            rvHorizontalList.adapter = seachPermitListAdapter
        }else{
            rvHorizontalList.visibility=View.INVISIBLE
        }
        }

        var salecount=searchList.get(position).maxSaleQtyAllowed-searchList.get(position).saleCount
        var available=searchList.get(position).maxSaleQtyAllowed-searchList.get(position).saleCount


        if ((searchList.get(position).productTypeID!=1)&&(searchList.get(position).activityType!="Day Pass")){
//            tvPermitAvailable.visibility=View.VISIBLE
//            if (searchList.get(position).rluPropertyModel.maxSaleQtyAllowed==searchList.get(position).rluPropertyModel.saleCount){
//                tvPermitAvailable.setText("Available "+"0")
                if (salecount<=0){
                    tvPermitAvailable.setText("Available "+"0")
                }else{
                    tvPermitAvailable.visibility=View.VISIBLE
                    tvPermitAvailable.setText("Available "+salecount+" of "+searchList.get(position).maxSaleQtyAllowed)
                }
//            }
        }else{
            tvPermitAvailable.visibility=View.INVISIBLE
        }



        if (searchList.get(position).status=="Open"){
            if(searchList.get(position).propertyUserStatus!=0){
                tvPreApproval.setText("PRE-APPROVAL REQUIRED")
                tvPreApproval.visibility=View.VISIBLE
            }
            else{
                tvPreApproval.visibility=View.INVISIBLE
            }
        }


        if (searchList.get(position).productTypeID!=1){
            if (searchList.get(position).saleCount>=searchList.get(position).maxSaleQtyAllowed
                && searchList.get(position).activityType!="Day Pass"){
                tvSoldOut.visibility=View.VISIBLE
            }else{
                tvSoldOut.visibility=View.INVISIBLE
            }
        }else{
            tvSoldOut.visibility=View.INVISIBLE
        }
                llPermitGridItem.setOnClickListener {
                 onItemClickListener.onGridClick(position)
        }

        return convertView

    }

    interface OnGridItemClickListener {
        fun onGridClick(index: Int)
    }

}