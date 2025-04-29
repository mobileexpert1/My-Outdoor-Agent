package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licensedetails.forhome.Model
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.special_for_you_recycler_item.view.*

class PropertyLicenceAdapter(val list: List<Model>,
                             //val onItemClickListener: ProductsListAdapter.OnItemClickListener
                             ) :
    RecyclerView.Adapter<PropertyLicenceAdapter.MyView>() {

    var row_index = 0

    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var llSimilarPropertiesItem: LinearLayout
        var ivSPImage: ImageView
        var tvSProperties: TextView
        var tvDisplayName: TextView
        init {
            llSimilarPropertiesItem = view.findViewById<LinearLayout>(R.id.llSimilarPropertiesItem)
            ivSPImage = view.findViewById<ImageView>(R.id.ivSPImage)
            tvSProperties = view.findViewById<TextView>(R.id.tvSProperties)
            tvDisplayName = view.findViewById<TextView>(R.id.tvDisplayName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.licence_now_similar_properties, parent, false)
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
//        val listData = list[position]
        Picasso.get().load(Constants.PROPERTY_IMAGE_URL + list!!.get(position).similarProperties.get(position).imageFilename)
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .centerCrop()
            .fit()
            .into(holder.ivSPImage)
        holder.tvSProperties.text = list[position].similarProperties.get(position).displayText
        holder.tvDisplayName.text = list[position].similarProperties.get(position).productNo
        /*holder.llSimilarPropertiesItem.setOnClickListener {

        }*/

    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onClick(index: Int)
    }
}