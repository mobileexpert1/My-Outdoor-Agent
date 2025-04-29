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

class VehichleDetailsAdapter(
    var modelList: ArrayList<VehicleDetails>,
    val activity: FragmentActivity?,
    var myAccountFragment: LicenceAnotherFragment
) : RecyclerView.Adapter<VehichleDetailsAdapter.ViewHolder>() {

    lateinit var commonList: ArrayList<String>

    var builder: AlertDialog.Builder? = null


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvMake: TextView = itemView.findViewById(R.id.tvVehicleMake)
        var tvModel: TextView = itemView.findViewById(R.id.tvVehicleModel)
        var tvColor: TextView = itemView.findViewById(R.id.tvColorDetails)
        var tvLicensePlate: TextView = itemView.findViewById(R.id.tvVehicleLicencePlate)

        //        var licencePlate :TextView =itemView.findViewById(R.id.licencePlate)
        var ivDeleteVehicle: ImageView = itemView.findViewById(R.id.ivDeleteVehicleDetails)
    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_details_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMake.isSelected = true
        holder.tvModel.isSelected = true
        holder.tvColor.isSelected = true
        holder.tvLicensePlate.isSelected = true
        holder.tvMake.setText(modelList.get(position).vehicleMake)
        holder.tvModel.setText(modelList.get(position).vehicleModel)
        holder.tvColor.setText(modelList.get(position).vehicleColor)
        holder.tvLicensePlate.setText(modelList.get(position).vehicleLicensePlate)

        holder.ivDeleteVehicle.setOnClickListener {
            showAddDeleteDialog(position)
        }

    }


    fun showAddDeleteDialog(position: Int) {
        builder = AlertDialog.Builder(activity!!)
        builder?.setTitle("Remove")
        builder?.setMessage("Are you sure you want to remove vehicle details?")
            ?.setCancelable(false)
            ?.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                if (modelList.size > 0) {
                    // hit api
                    myAccountFragment!!.removeitem("" + modelList.get(position).vehicleDetailID)
                    modelList.removeAt(position)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
            })
            ?.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.show()
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}