package com.myoutdoor.agent.adapter

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.myoutdoor.agent.R

class SecondLevelAdapter(
    context: Context?,
    headers: Array<String>,
    childData: MutableList<Array<String>>?
) : BaseExpandableListAdapter() {

    var context: Context? = null

    var data: List<Array<String>>? = null

  var headers: Array<String>? = null


    init {
        this.context =context
        this.headers= headers
        this.data=childData

    }

    override fun getGroup(groupPosition: Int): Any {
        return headers!![groupPosition]!!
    }

    override fun getGroupCount(): Int {
        return headers!!.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = convertView
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.row_second, null)
        val text = convertView!!.findViewById<View>(R.id.rowSecondText) as TextView
        val groupText = getGroup(groupPosition).toString()
        text.text = groupText

        val tvExpandableListviewDescription = convertView!!.findViewById<TextView>(R.id.tvExpandableListviewDescription)
        val ivDownup=convertView!!.findViewById<ImageView>(R.id.ivDownup)
        val ivClick = convertView!!.findViewById<ImageView>(R.id.ivClick)
//        tvExpandableListviewDescription.text = data!![groupPosition][0]
      //  tvExpandableListviewDescription.movementMethod = ScrollingMovementMethod()
        /*ivClick.setOnClickListener {
            tvExpandableListviewDescription.visibility=View.VISIBLE
            ivDownup.visibility=View.VISIBLE
            ivClick.visibility=View.GONE
        }

        ivDownup.setOnClickListener {
            tvExpandableListviewDescription.visibility=View.GONE
            ivClick.visibility=View.VISIBLE
            ivDownup.visibility=View.GONE
        }*/
        return convertView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        val childData: Array<String>
        childData = data!![groupPosition]
        return childData[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = convertView
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.row_third, null)
        val textView = convertView!!.findViewById<View>(R.id.rowThirdText) as TextView
        val childArray = data!![groupPosition]
        val text = childArray[0]
        textView.text = text
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val children = data!![groupPosition]
        return children.size
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}