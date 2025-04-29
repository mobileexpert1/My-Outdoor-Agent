package com.myoutdoor.agent.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licence.newModel.response.Amenity
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso

class LicenseNowAmenityAdapter(var modelList: List<Amenity>,
                               val activity: FragmentActivity?,
): RecyclerView.Adapter<LicenseNowAmenityAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var ivAmenityIcon: ImageView = itemView.findViewById(R.id.ivAmenityIcon)
        var tvAmenityName: TextView = itemView.findViewById(R.id.tvAmenityName)
        var tvAmenityDescription: TextView = itemView.findViewById(R.id.tvAmenityDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.license_amenity_list_item, parent, false)
        return LicenseNowAmenityAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(Constants.LICENSE_AMENITIES_URL + modelList!!.get(position).amenityIcon)
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .into(holder.ivAmenityIcon)
        holder.tvAmenityName.setText(modelList.get(position).amenityName)
      //  Log.e("####","description...."+modelList.get(position).description)
        holder.tvAmenityDescription.setText(modelList.get(position).description)

    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}