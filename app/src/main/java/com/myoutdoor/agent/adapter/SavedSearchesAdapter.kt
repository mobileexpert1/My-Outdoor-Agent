package com.myoutdoor.agent.adapter

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.fragment.UserProfile.MyAccountFragment
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.savedsearches.getsavedsearches.Model


class SavedSearchesAdapter(
    var modelList: ArrayList<Model>,
    val activity: FragmentActivity?,
    var onItemClickListener: OnItemClickListener,
    var myAccountFragment: MyAccountFragment,


    ): RecyclerView.Adapter<SavedSearchesAdapter.ViewHolder>() {

    lateinit var commonList: ArrayList<String>

    companion object {
        lateinit var mainActivity: MainActivity
    }

    var builder: AlertDialog.Builder? = null


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var rvSavedSearchesList: RecyclerView = itemView.findViewById(R.id.rvSavedSearchesList)
        var linearTop: LinearLayout = itemView.findViewById(R.id.linearTop)
        var ivDeleteSearches: ImageView = itemView.findViewById(R.id.ivDeleteSearches)
        var ivViewSearches: ImageView = itemView.findViewById(R.id.ivViewSearches)
    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_searches_list_item, parent, false)
        return SavedSearchesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        commonList= ArrayList()

        if (modelList.get(position).RegionName.size > 0){
            commonList.addAll(modelList.get(position).RegionName)
        }

        if (modelList.get(position).PropertyName.size > 0){
            commonList.addAll(modelList.get(position).PropertyName)
        }

        if (modelList.get(position).StateName.size > 0){
            commonList.addAll(modelList.get(position).StateName)
        }

        if (modelList.get(position).Amenities.size > 0){
            commonList.addAll(modelList.get(position).Amenities)
        }

        if (modelList.get(position).SearchName!=null) {
            commonList.addAll(listOf(modelList.get(position).SearchName))
        }

        var savedSearchesInnerAdapter = SavedSearchesInnerAdapter(commonList, activity)
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        holder.rvSavedSearchesList.setLayoutManager(manager)
        holder.rvSavedSearchesList.setAdapter(savedSearchesInnerAdapter)


        holder.ivDeleteSearches.setOnClickListener {
            showAddDeleteDialog(position)
        }


        holder.ivViewSearches.setOnClickListener {
            MainActivity.mainActivity.bottomNav.setSelectedItemId(R.id.search);
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
        }

        /* holder.ivViewSearches.setOnClickListener {

            //myAccountFragment.replaceFragment(mainActivity,R.id.container,true)

         }
 */
    }

    fun showAddDeleteDialog(position: Int) {
        builder = AlertDialog.Builder(activity!!)
        builder?.setMessage("Are you sure you want to delete this search?")
            ?.setCancelable(false)
            ?.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                if (modelList.size > 0) {
                    // hit api
                    myAccountFragment!!.removeitem(""+modelList.get(position).UserSearchID)
                    modelList.removeAt(position)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
            })
            ?.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert: AlertDialog = builder!!.create()
        alert.setOnShowListener(DialogInterface.OnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(activity.getResources().getColor(R.color.green))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(activity.getResources().getColor(R.color.red_pre_approval))
        })

        alert.show()
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}