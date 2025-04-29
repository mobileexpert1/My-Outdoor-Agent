package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_chat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        val ItemsViewModel = mList[position]
//
//        holder.permitTV.text = ItemsViewModel.permitTV
//        holder.dateTV.text=ItemsViewModel.dateTV
//        holder.permitTV.text=ItemsViewModel.permitTV
    }
    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val receiverTV: TextView = itemView.findViewById(R.id.receiverTV)
        val senderTV: TextView = itemView.findViewById(R.id.senderTV)
        val receiverTimeTV: TextView = itemView.findViewById(R.id.receiverTimeTV)
        val sendTimeTV: TextView = itemView.findViewById(R.id.sendTimeTV)
    }}