package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R


import com.myoutdoor.agent.models.message.Model

class ProductsListAdapter(private var modelList: List<Model>,
                          activity: FragmentActivity?,
                          var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProductsListAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.products_list_items, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (modelList.get(position).unreadCount > 0){
            holder.tvCounter.visibility = View.VISIBLE
            holder.tvCounter.setText(""+modelList.get(position).unreadCount)
        }else {
            holder.tvCounter.visibility = View.GONE
        }

        holder.tvPermit.text = modelList[position].productNo
        holder.tvDateTime.text = modelList[position].postedDate

        holder.itemView.setOnClickListener {

        }

        holder.itemView.setOnClickListener { view: View? ->
            onItemClickListener.onChatListListener(
                position, modelList[position].productID.toString(), modelList[position].productNo.toString()
            )
        }

        /*val ItemsViewModel = mList[position]
        holder.permitTV.text = ItemsViewModel.permitTV
        holder.dateTV.text=ItemsViewModel.dateTV
        holder.timeTV.text=ItemsViewModel.timeTV*/
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvPermit: TextView = itemView.findViewById(R.id.tvPermit)
        var tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        var tvCounter: TextView = itemView.findViewById(R.id.tvCounter)
    }

    interface OnItemClickListener {
        fun onChatListListener(index: Int, productID: String, productNo: String)
    }
}