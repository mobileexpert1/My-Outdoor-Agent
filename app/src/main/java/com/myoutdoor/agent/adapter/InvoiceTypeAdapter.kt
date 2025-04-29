package com.myoutdoor.agent.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.example.AdditionalInvoices
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.LicenceAnotherFragment


class InvoiceTypeAdapter(var modelList: ArrayList<AdditionalInvoices>,
                         val activity: FragmentActivity?,
                         var myAccountFragment: LicenceAnotherFragment,onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<InvoiceTypeAdapter.ViewHolder>() {
    private var onItemClick: OnItemClickListener? = null

    fun setOnItemClick(onItemClick: OnItemClickListener?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invoices_paynow_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var data=modelList[position]
        holder.apply {
            try {
                tvInvoiceType.setText(data.typeName)
                tvAmountInvoice.setText(data.amount)

                if (data.invoicePaid==true){
                    tvPayNowInvoice.text="Paid"
                }else {
                    tvPayNowInvoice.text="Pay Now"
                    tvPayNowInvoice.setTextColor(R.color.white)
                    tvPayNowInvoice.setBackgroundResource(R.drawable.login_register_shape_green)
                }
                tvPayNowInvoice.setOnClickListener {
                    Log.e("@@","Clicked.....")
                    onItemClick!!.onClickListListener(position)

                }
            }catch (e:Exception){
                Log.e("call","exception: "+e.toString())
            }

        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvInvoiceType: TextView = itemView.findViewById(R.id.tvInvoiceType)
        var tvAmountInvoice: TextView = itemView.findViewById(R.id.tvAmountInvoice)
        var tvPayNowInvoice: TextView = itemView.findViewById(R.id.tvPayNowInvoice)
    }


    interface OnItemClickListener {
        fun onClickListListener(index: Int)
    }
}
