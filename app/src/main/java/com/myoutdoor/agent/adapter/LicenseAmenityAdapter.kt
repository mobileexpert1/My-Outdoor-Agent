package com.myoutdoor.agent.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licensedetails.formylicense.license.Amenity
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso

class LicenseAmenityAdapter(
//    var modelList: ArrayList<com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.Amenity>,
    var modelList: ArrayList<Amenity>,
    val activity: FragmentActivity?,
): RecyclerView.Adapter<LicenseAmenityAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var ivAmenityIcon: ImageView = itemView.findViewById(R.id.ivAmenityIcon)
        var tvAmenityName: TextView = itemView.findViewById(R.id.tvAmenityName)
        var tvAmenityDescription: TextView = itemView.findViewById(R.id.tvAmenityDescription)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.license_amenity_list_item, parent, false)
        return LicenseAmenityAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.tvAmenityDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
         holder.tvAmenityDescription.setSelected(true);
         holder.tvAmenityDescription.setSingleLine(true);


        Picasso.get().load(Constants.LICENSE_AMENITIES_URL + modelList!!.get(position).amenityIcon)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.ivAmenityIcon)
            holder.tvAmenityName.setText(modelList.get(position).amenityName)
            holder.tvAmenityDescription.setText(modelList.get(position).description)







    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}