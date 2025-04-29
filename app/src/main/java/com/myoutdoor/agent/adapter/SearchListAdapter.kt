package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.savedsearches.searchautofill.Model

class SearchListAdapter(private var modelList: List<Model>,
                        activity: FragmentActivity?,
                        var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_textview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvList.text = modelList[position].searchResult

        holder.itemView.setOnClickListener {

        }

        holder.tvList.setOnClickListener { view: View? ->
            onItemClickListener.onClickListListener(
                position
            )
        }


    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvList: TextView = itemView.findViewById(R.id.tvList)

    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }
}