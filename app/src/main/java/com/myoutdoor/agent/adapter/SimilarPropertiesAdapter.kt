package com.myoutdoor.agent.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licence.newModel.response.SimilarProperty
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso

class SimilarPropertiesAdapter(val list: ArrayList<SimilarProperty>,
                               val onItemClickListener: SimilarPropertiesAdapter.OnItemClickListener) :
    RecyclerView.Adapter<SimilarPropertiesAdapter.MyView>() {

    var row_index = 0

    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var tvSProperties: TextView
        var tvDisplayName: TextView
        var ivSPImage: ImageView
        var llViewDetails: LinearLayout
        init {
            ivSPImage = view.findViewById<ImageView>(R.id.ivSPImage)
            tvSProperties = view.findViewById<TextView>(R.id.tvSProperties)
            tvDisplayName = view.findViewById<TextView>(R.id.tvDisplayName)
            llViewDetails = view.findViewById<LinearLayout>(R.id.llViewDetails)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.licence_now_similar_properties, parent, false)
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]
        holder.tvDisplayName.text = listData.displayText
        holder.tvSProperties.text = listData.productNo

        Picasso.get().load(Constants.PROPERTY_IMAGE_URL + list!!.get(position).imageFilename)
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .centerCrop()
            .fit()
            .into(holder.ivSPImage)
        holder.tvDisplayName.text = list.get(position).countyName+"County,"+list.get(position).stateName
        holder.tvSProperties.text = list.get(position).productNo

        holder.llViewDetails.setOnClickListener {
            row_index=position
            onItemClickListener.onChatListListener(position)
            notifyDataSetChanged()
        }

        /*if(row_index==position){
            holder.itemView.tvGreenButton.setBackgroundResource(R.drawable.login_register_shape_green)
            holder.itemView.tvGreenButton.setTextColor(Color.parseColor("#ffffff"))
        }
        else
        {
            holder.itemView.tvGreenButton.setBackgroundResource(R.drawable.my_licences_white_button)
            holder.itemView.tvGreenButton.setTextColor(Color.parseColor("#FF000000"))
        }*/

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onChatListListener(index: Int)
    }

}