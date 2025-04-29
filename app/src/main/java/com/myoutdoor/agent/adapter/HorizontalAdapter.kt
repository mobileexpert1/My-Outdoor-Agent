package com.myoutdoor.agent.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import kotlinx.android.synthetic.main.special_for_you_recycler_item.view.*

class HorizontalAdapter (val list: ArrayList<String>,
                         val onItemClickListener: HorizontalAdapter.OnItemClickListener) :
    RecyclerView.Adapter<HorizontalAdapter.MyView>() {

    var row_index = 0

    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView
        init {
            textView = view
                .findViewById<TextView>(R.id.tvGreenButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.special_for_you_recycler_item, parent, false)
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]
        holder.textView.text = list[position]
        holder.textView.setOnClickListener {
            row_index=position
            onItemClickListener.onChatListListener(position)
            notifyDataSetChanged()
        }

        if(row_index==position){
            holder.itemView.tvGreenButton.setBackgroundResource(R.drawable.login_register_shape_green)
            holder.itemView.tvGreenButton.setTextColor(Color.parseColor("#ffffff"))
        }
        else
        {
            holder.itemView.tvGreenButton.setBackgroundResource(R.drawable.my_licences_white_button)
            holder.itemView.tvGreenButton.setTextColor(Color.parseColor("#FF000000"))
        }

   }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onChatListListener(index: Int)
    }

}