package com.myoutdoor.agent.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.search.AmenityName
import com.myoutdoor.agent.models.search.searchV2.Amenitiy
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso
import java.util.ArrayList

//class SearchPermitListAdapter(var amenityNameList: List<AmenityName>
class SearchPermitListAdapter(var amenityNameList: List<Amenitiy>
): RecyclerView.Adapter<SearchPermitListAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var ivRlUImages: ImageView = itemView.findViewById(R.id.ivRlUImages)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_permit_horizontal_list, parent, false)
        return SearchPermitListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (amenityNameList.get(position).amenityIcon!=null){
            Log.e("call","@@@@url     "+Constants.LICENSE_AMENITIES_URL + amenityNameList.get(position).amenityIcon)
            Picasso.get().load(Constants.LICENSE_AMENITIES_URL + amenityNameList.get(position).amenityIcon)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.ivRlUImages)
        }

    }

    override fun getItemCount(): Int {
        return amenityNameList.size
    }

}