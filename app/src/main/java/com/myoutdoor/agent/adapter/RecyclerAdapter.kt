package com.myoutdoor.agent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val mDataset: Array<String?>, private val mContext: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView = itemView.findViewById<View>(android.R.id.text1) as TextView
    }
    override fun getItemCount(): Int {
        return mDataset.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = mDataset[position]
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1, parent, false
        )
        return ViewHolder(view)
    }
}