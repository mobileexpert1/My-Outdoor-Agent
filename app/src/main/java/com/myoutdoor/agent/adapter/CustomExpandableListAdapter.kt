package com.myoutdoor.agent.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.myoutdoor.agent.R
import java.util.HashMap

class CustomExpandableListAdapter internal constructor(
   private val context: Context,
   private val titleList: List<String>,
   private val dataList: HashMap<String, List<String>>
   ) : BaseExpandableListAdapter() {

      override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
         return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
      }

      override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
         return expandedListPosition.toLong()
      }

      override fun getChildView(
      listPosition: Int,
      expandedListPosition: Int,
      isLastChild: Boolean,
      convertView: View?,
      parent: ViewGroup
      ): View {
         var convertView = convertView
         val expandedListText = getChild(listPosition, expandedListPosition) as String
         if (convertView == null) {
            val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.expandable_listview_box, null)
         }
         val expandedListTextView = convertView!!.findViewById<TextView>(R.id.tvExpandableListview)
         val tvExpandableListviewDescription = convertView!!.findViewById<TextView>(R.id.tvExpandableListviewDescription)
         val ivClick = convertView!!.findViewById<ImageView>(R.id.ivClick)
         ivClick.setOnClickListener {
            tvExpandableListviewDescription.visibility=View.VISIBLE
         }
         expandedListTextView.text = expandedListText
         return convertView
      }
      override fun getChildrenCount(listPosition: Int): Int {
         return this.dataList[this.titleList[listPosition]]!!.size
      }
      override fun getGroup(listPosition: Int): Any {
         return this.titleList[listPosition]
      }
      override fun getGroupCount(): Int {
         return this.titleList.size
      }
      override fun getGroupId(listPosition: Int): Long {
         return listPosition.toLong()
      }
      override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
      var convertView = convertView
      val listTitle = getGroup(listPosition) as String
      if (convertView == null) {
         val layoutInflater =
         this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
      }

      var listTitleTextView = convertView!!.findViewById<TextView>(R.id.tvWhiteBanner)
      var ivMainDropDown = convertView!!.findViewById<ImageView>(R.id.ivMainDropDown)
/*      var llMainClick = convertView!!.findViewById<LinearLayout>(R.id.llMainClick)
         ivMainDropDown.setOnClickListener {
         } */
//    istTitleTextView.setTypeface(null, Typeface.BOLD)
      listTitleTextView.text = listTitle
      return convertView
   }

   override fun hasStableIds(): Boolean {
      return false
   }
   override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
      return true
  }
}