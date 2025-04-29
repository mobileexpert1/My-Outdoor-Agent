package com.myoutdoor.agent.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.LicenceAnotherFragment
import com.myoutdoor.agent.models.licensedetails.formylicense.license.VehicleDetails

class InviteMemberListAdapter(
    var modelList: ArrayList<VehicleDetails>,
    val activity: FragmentActivity?,
    var myAccountFragment: LicenceAnotherFragment
): RecyclerView.Adapter<InviteMemberListAdapter.ViewHolder>() {

    lateinit var commonList: ArrayList<String>

    var builder: AlertDialog.Builder? = null


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvMake: TextView = itemView.findViewById(R.id.tvMake)
        var tvModel: TextView = itemView.findViewById(R.id.tvModel)
        var tvColor: TextView = itemView.findViewById(R.id.tvColor)
//        var tvLicensePlate: TextView = itemView.findViewById(R.id.tvLicensePlate)
//        var ivDeleteVehicle: ImageView = itemView.findViewById(R.id.ivDeleteVehicle)
    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_current_members, parent, false)
        return InviteMemberListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvMake.setText(modelList.get(position).vehicleMake)
        holder.tvModel.setText(modelList.get(position).vehicleModel)
        holder.tvColor.setText(modelList.get(position).vehicleColor)
//        holder.tvLicensePlate.setText(modelList.get(position).vehicleLicensePlate)
//
//        holder.ivDeleteVehicle.setOnClickListener {
//            showAddDeleteDialog(position)
//        }

    }


    fun showAddDeleteDialog(position: Int) {
        builder = AlertDialog.Builder(activity!!)
        builder?.setMessage("")
            ?.setCancelable(false)
            ?.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, id ->
                if (modelList.size > 0) {
                    // hit api
                    myAccountFragment!!.removeitem(""+modelList.get(position).vehicleDetailID)
                    modelList.removeAt(position)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
            })
            ?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.setTitle("Are you want to delete item")
        alert.show()
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}