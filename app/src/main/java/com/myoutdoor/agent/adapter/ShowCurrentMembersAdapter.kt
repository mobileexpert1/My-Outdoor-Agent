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
import com.myoutdoor.agent.models.licensedetails.formylicense.license.LicenseDetails
import com.myoutdoor.agent.models.licensedetails.formylicense.license.LicenseMember

class ShowCurrentMembersAdapter(
    var listMembers: ArrayList<LicenseMember>,
    val activity: FragmentActivity?,
    var myAccountFragment: LicenceAnotherFragment,
//    var memberDetailsModel: com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.LicenseDetails
    var memberDetailsModel: LicenseDetails
): RecyclerView.Adapter<ShowCurrentMembersAdapter.ViewHolder>() {

    lateinit var commonList: ArrayList<String>

    var builder: AlertDialog.Builder? = null


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        var tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        var tvMember: TextView = itemView.findViewById(R.id.tvMember)
        var tvInvite: TextView = itemView.findViewById(R.id.tvInvite)
        var ivDeleteAddMember: ImageView = itemView.findViewById(R.id.ivDeleteAddMember)

    }

    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_current_members, parent, false)
        return ShowCurrentMembersAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.tvName.isSelected=true
        holder.tvEmail.isSelected=true
        holder.tvPhone.isSelected=true
        holder.tvInvite.isSelected=true

        if (listMembers.get(position).isAccepted == false){
            // visibility gone
            holder.tvInvite.visibility = View.VISIBLE
        }else {
            // visibility visible
            holder.tvInvite.visibility = View.GONE
        }

        if (memberDetailsModel.allowMemberActions == true){
            // visibility gone
            holder.ivDeleteAddMember.visibility = View.VISIBLE
        }else {
            // visibility visible
            holder.ivDeleteAddMember.visibility = View.GONE
        }

        holder.tvName.setText(listMembers.get(position).firstName+" "+listMembers.get(position).lastName)
        holder.tvEmail.setText(listMembers.get(position).email)
//        holder.tvPhone.setText("")
         holder.tvPhone.setText(listMembers.get(position).phone.toString())

        holder.ivDeleteAddMember.setOnClickListener {
            showAddDeleteDialog(position)
        }

    }


    fun showAddDeleteDialog(position: Int) {
        builder = AlertDialog.Builder(activity!!)
        builder?.setTitle("Remove")
        builder?.setMessage("Are you sure you want to remove from member list.")
            ?.setCancelable(false)
            ?.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                if (listMembers.size > 0) {
                    // hit api
                    myAccountFragment!!.removeMember(""+listMembers.get(position).licenseContractMemberID)
                    listMembers.removeAt(position)
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
        return listMembers.size
    }
}