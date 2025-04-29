package com.myoutdoor.agent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import java.util.ArrayList


class SavedSearchesInnerAdapter(var commonList: ArrayList<String>,
                                val activity: FragmentActivity?
): RecyclerView.Adapter<SavedSearchesInnerAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvGreenButton: TextView = itemView.findViewById(R.id.tvGreenButton)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_seraches_inner_list, parent, false)
        return SavedSearchesInnerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvGreenButton.setText(""+commonList.get(position))

    }

    override fun getItemCount(): Int {
        return commonList.size

    }
}