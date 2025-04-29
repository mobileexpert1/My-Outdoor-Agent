package com.myoutdoor.agent.fragment.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.adapter.ProductsListAdapter

import com.myoutdoor.agent.fragment.home.HomeFragment
import com.myoutdoor.agent.fragment.message_new.MessageNewFragment
import com.myoutdoor.agent.models.message.Model
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_property_list.*
import kotlinx.android.synthetic.main.toolbar.*


class PropertyListFragment : BaseFragment(),ProductsListAdapter.OnItemClickListener {

    private var propertyadapter: RecyclerView.Adapter<ProductsListAdapter.ViewHolder>? = null
    private val permitList: ArrayList<String>? = null
    private val dateList: ArrayList<String>?= null
    private val timeList: ArrayList<String>?= null
    lateinit var messageResponseList: ArrayList<Model>
    lateinit var viewModel: MessageViewModel
    lateinit var pref: SharedPref


companion object {
    lateinit var propertyListFragment: PropertyListFragment
}

    override fun getLayoutId(): Int {
       return R.layout.fragment_property_list
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView() {
        propertyListFragment = this

        HomeFragment.homeFragment.pvSearchSelect.dismiss()
        pref= SharedPref(requireContext())

        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

        messageResponseList = ArrayList()

        tvToolbar.setText("Property List")
//        backpress button
        ivBackpress.visibility=View.INVISIBLE
        ivBackpress.setBackgroundColor(R.color.light_white_home_screen)
        addItems();
        productsRV.layoutManager = LinearLayoutManager(activity)
        propertyadapter =  ProductsListAdapter(messageResponseList,activity,this)
        productsRV.adapter = propertyadapter

        setObserver()

        viewModel.messageRequest(pref.getString(Constants.TOKEN))

    }


    fun setObserver() {
        viewModel.messageSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call","list "+it!!.model.toString())
            messageResponseList.clear()
            messageResponseList.addAll(it!!.model)
            propertyadapter?.notifyDataSetChanged()

        }
      )

        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
          //  showShortToast(it)
            if (messageResponseList.size == 0){
                productsRV.visibility = View.GONE
                tvNoPropertyDataFound.visibility = View.VISIBLE
            }
        }
        )

      viewModel.isLoading.observe(requireActivity(), Observer {
               Log.d("@@@@", "Failed")
               if (it) {
                   progressBarPB.show()
               }
               else {
                   progressBarPB.dismiss()
               }
        }
      )

    }


    private fun addItems() {


    }

    override fun onChatListListener(index: Int, productID: String, productNo: String) {

      //  showShortToast("Comming Soon")
        val fragment = MessageNewFragment()
        val bundle = Bundle()
        bundle.putString("productId", productID)
        bundle.putString("productNo", productNo)
        fragment.setArguments(bundle)
        addNewFragment(fragment, R.id.container, true)
    }

    fun refreshApi_(){
        viewModel.messageRequest(pref.getString(Constants.TOKEN))
    }

}