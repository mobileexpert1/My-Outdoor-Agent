package com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.ClientDocument

class DocumentAdapterr(var list:List<ClientDocument>, val callback: (Int, ClientDocument) -> Unit): RecyclerView.Adapter<DocumentAdapterr.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = list[position]
        holder.apply {
            tvDocName.text= data.fileName
            itemView.setOnClickListener {
                callback.invoke(position,data)
            }
        }

    }

}