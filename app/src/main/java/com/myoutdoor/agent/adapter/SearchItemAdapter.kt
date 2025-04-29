package com.myoutdoor.agent.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.search.SearchFragment

class SearchItemAdapter(var searchFragment: SearchFragment,
                        var list: ArrayList<com.myoutdoor.agent.models.savedsearches.searchautofill.Model>,
                        var callback: (com.myoutdoor.agent.models.savedsearches.searchautofill.Model) -> Unit
    ) :
    RecyclerView.Adapter<SearchItemAdapter.ViewHolder>()
{

    val dataList = list.distinct()
    private var activity: FragmentActivity? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_textview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvName.setText(""+dataList.get(position).searchResult)

        holder.itemView.setOnClickListener {
            try {
                callback.invoke(dataList[position])
//                searchFragment.searchClickItem(dataList.get(position).searchResult,dataList.get(position).type)
            }catch (e:Exception){

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvList)

    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }
}