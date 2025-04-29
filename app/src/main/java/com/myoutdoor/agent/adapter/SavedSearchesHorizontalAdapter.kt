package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.savedsearches.getsavedsearches.Model
import kotlinx.android.synthetic.main.special_for_you_recycler_item.view.*

class SavedSearchesHorizontalAdapter(var modelList: List<Model>

) :
    RecyclerView.Adapter<SavedSearchesHorizontalAdapter.MyView>() {
    var row_index = 0
    lateinit var data: ArrayList<String>

    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var tvGreenButton: TextView
        init {
            tvGreenButton = view.findViewById<TextView>(R.id.tvGreenButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.special_for_you_recycler_item, parent, false)
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = modelList.get(position)
//        for (i in modelList.indices){
            holder.tvGreenButton.text = modelList.get(position).Amenities.toString()

//        }


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
        return modelList.size
    }


    interface OnItemClickListener {
        fun onChatListListener(index: Int)
    }

}