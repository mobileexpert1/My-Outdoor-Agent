package com.myoutdoor.agent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.getallamenities.Model
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso

class SelectActivitiesAdapter(
    private val context: Context,
    private var activityList: ArrayList<Model>,
    val onItemClickListener: OnItemClickListener,
    var searchFragment: SearchFragment,
) :
    BaseAdapter()
{
    private var layoutInflater: LayoutInflater? = null
    private lateinit var llActivityAmenity: LinearLayout
    private lateinit var tvActivityAmenity: TextView
    private lateinit var ivSelectedImage: ImageView
    private lateinit var ivCheck: ImageView
    private lateinit var activityList1:ArrayList<String>

    override fun getCount(): Int {
            return activityList.size
    }

    override fun getItem(position: Int): Any? {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.select_amenities_activities_list_item, null)
        }

        llActivityAmenity = convertView!!.findViewById(R.id.llActivityAmenity)
        ivCheck = convertView!!.findViewById(R.id.ivCheck)
        ivSelectedImage = convertView!!.findViewById(R.id.ivSelectedImage)
        tvActivityAmenity = convertView!!.findViewById(R.id.tvActivityAmenity)


        if (activityList.get(position).isSelected == true){
            ivCheck.visibility = View.VISIBLE
        }else {
            ivCheck.visibility = View.GONE
        }

        llActivityAmenity.setOnClickListener {

            if (activityList.get(position).isSelected == true){
                activityList.get(position).isSelected = false
                searchFragment.selectedItemsActivity.remove(activityList.get(position).amenityName)
            }else {
                activityList.get(position).isSelected = true
                searchFragment.selectedItemsActivity.add(activityList.get(position).amenityName)
            }

            onItemClickListener.onClick(position)
        }

        tvActivityAmenity.setText(activityList.get(position).amenityName)
        Picasso.get().load(Constants.AMENITIES_URL + activityList!!.get(position).amenityIcon)
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .into(ivSelectedImage)

        return convertView
    }

    interface OnItemClickListener {
        fun onClick(index: Int)
    }

}