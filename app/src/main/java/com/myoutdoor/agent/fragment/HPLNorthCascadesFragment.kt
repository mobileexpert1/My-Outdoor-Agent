package com.myoutdoor.agent.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.adapter.ChatAdapter
import com.myoutdoor.agent.adapter.ProductsListAdapter
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_h_p_l_north_cascades.*
import kotlinx.android.synthetic.main.toolbar.*

class HPLNorthCascadesFragment : BaseFragment() {

    private var adapter: RecyclerView.Adapter<ProductsListAdapter.ViewHolder>? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_h_p_l_north_cascades
    }

    override fun onCreateView() {
        tvToolbar.setText("HPL - North Cascades")
//        backpress button
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }
        chatmessageRV.layoutManager = LinearLayoutManager(activity)
        val adapter = ChatAdapter()
        chatmessageRV.adapter = adapter
    }

}