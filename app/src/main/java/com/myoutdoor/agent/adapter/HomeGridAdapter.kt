package com.myoutdoor.agent.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.home.HomeFragment
import com.myoutdoor.agent.models.home.Model
import com.myoutdoor.agent.models.home.RLURegionModel
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso

class HomeGridAdapter(
    private var regionListData: ArrayList<RLURegionModel>,
    private val context: HomeFragment,
    val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<HomeGridAdapter.ViewHolder>() {

    private lateinit var llHomeScreenGridItem: LinearLayout
    private lateinit var ivImageData: ImageView
    private lateinit var tvLocation: TextView
    private lateinit var tvDisplayName: TextView
    private lateinit var rlViewDetails: RelativeLayout

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            llHomeScreenGridItem = itemView.findViewById(R.id.llHomeScreenGridItem)
            ivImageData = itemView.findViewById(R.id.ivImageData)
            tvLocation = itemView.findViewById(R.id.tvLocation)
            tvDisplayName = itemView.findViewById(R.id.tvDisplayName)
            rlViewDetails = itemView.findViewById(R.id.rlViewDetails)
        }
    }

//    public fun notifyAdapter(horizontalPosition: Int) {
//        this.horizontalPosition = horizontalPosition
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_screen_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        tvLocation.setText(regionListData.get(position).location)
        tvDisplayName.setText(regionListData.get(position).displayName)

        Log.e(
            "call",
            "123  " + Constants.PROPERTY_IMAGE_URL + regionListData!!.get(
                position
            ).imageFileName
        )

        if (regionListData.get(position).imageFileName != null) {
            Picasso.get().load(
                Constants.PROPERTY_IMAGE_URL + regionListData!!.get(
                    position
                ).imageFileName
            )
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(ivImageData)
        } else {
            Picasso.get().load(R.drawable.ic_logo)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(ivImageData)
        }


        rlViewDetails.setOnClickListener {
            onItemClickListener.onItemClick(position)

        }

    }

    override fun getItemCount(): Int {
            return regionListData.size

    }


    interface OnItemClickListener {
        fun onItemClick(index: Int)
    }


}