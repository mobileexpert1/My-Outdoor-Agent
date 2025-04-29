package com.myoutdoor.agent.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.licence.RenewMembers
import kotlinx.android.synthetic.main.fragment_my_account.*


class PreviousMemberAdapter(
    var renewalList: ArrayList<RenewMembers>,
    val activity: FragmentActivity?,
): RecyclerView.Adapter<PreviousMemberAdapter.ViewHolder>() {
    private var onItemClick: OnItemClickListener? = null

    fun setOnItemClick(onItemClick: OnItemClickListener?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.previous_memberlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.tvNamePrevious.setText(renewalList.get(position).firstName+"\n"+renewalList.get(position).lastName)
       holder.tvEmailPrevious.setText(renewalList.get(position).email)
       holder.tvPhonePrevious.setText(renewalList.get(position).phone)

        var accPhone: String = renewalList.get(position).phone

            if (accPhone != null) {
                if (accPhone.length >= 10) {
                    var phoneNumber = "" + accPhone
                    var open = "("
                    var close = ")"
                    var code = " "
                    code = open + phoneNumber.substring(
                        0,
                        3
                    ) + close + " " + phoneNumber.substring(
                        3,
                        6
                    ) + "-" + phoneNumber.substring(6, 10)
                    holder.tvPhonePrevious.setText(code)
                } else {
                    holder.tvPhonePrevious.setText(accPhone)

            }
        }

    }

    override fun getItemCount(): Int {
        return renewalList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvNamePrevious: TextView = itemView.findViewById(R.id.tvNamePrevious)
        var tvEmailPrevious: TextView = itemView.findViewById(R.id.tvEmailPrevious)
        var tvPhonePrevious: TextView = itemView.findViewById(R.id.tvPhonePrevious)
    }


    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }
}
