package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licensedetails.formylicense.license.SpecialCondition

class LicenceGuideLinesAdapter(
//    var modelList: List<com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.SpecialCondition>,
    var modelList: List<SpecialCondition>,
    val activity: FragmentActivity?,
): RecyclerView.Adapter<LicenceGuideLinesAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvSpecialConditionsGuidelines: TextView = itemView.findViewById(R.id.tvSpecialConditionsGuidelines)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.license_guideline_list_item, parent, false)
        return LicenceGuideLinesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvSpecialConditionsGuidelines.setText(modelList.get(position).specCndDesc)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}