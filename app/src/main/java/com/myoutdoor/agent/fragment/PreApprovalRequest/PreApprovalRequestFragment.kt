package com.myoutdoor.agent.fragment.PreApprovalRequest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.PreApprovalAdapter
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.models.preapprovalrequest.Data
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_pre_approval_request.*
import kotlinx.android.synthetic.main.toolbar.*


class PreApprovalRequestFragment : BaseFragment() {

    lateinit var viewModel: PreApprovalRequestViewModel
    lateinit var pref: SharedPref
    lateinit var preApprovalRequestResponseList: ArrayList<Data>
    lateinit var preApprovalAdapter: PreApprovalAdapter
    var position:Int = 0


    override fun getLayoutId(): Int {
        return R.layout.fragment_pre_approval_request
    }

    companion object {
        lateinit var preApprovalRequestFragment: PreApprovalRequestFragment
        var isBackPreApproval:Boolean = false
    }

    override fun onCreateView() {

        isBackPreApproval = true

        preApprovalRequestFragment = this

        pref = SharedPref(requireContext())

        viewModel = ViewModelProvider(this).get(PreApprovalRequestViewModel::class.java)

        preApprovalRequestResponseList = ArrayList()

        tvToolbar.setText("Pre Approval Request")
//        backpress button
        ivBackpress.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
            requireActivity().onBackPressed()
        }

        viewModel.preApprovalRequest(pref.getString(Constants.TOKEN))
        setObserver()



        fragmentBackPressHandle()
    }


    var productNo=""
    fun setObserver() {
        viewModel.preApprovalSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            preApprovalRequestResponseList.clear()
            preApprovalRequestResponseList.addAll(it!!.model)


            preApprovalAdapter = PreApprovalAdapter(
                requireActivity(),
                this@PreApprovalRequestFragment,
                preApprovalRequestResponseList
            )
            llPre_ApprovalItemsTab.adapter = preApprovalAdapter
            preApprovalAdapter.notifyDataSetChanged()


            // check for unactivated account

            }
        )

        viewModel.cancelRequestSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

//           preApprovalRequestResponseList.clear()
//           viewModel.preApprovalRequest(pref.getString(Constants.TOKEN))

            if (position == 0){
                tvNoDataFound.visibility = View.VISIBLE
                llPre_ApprovalItemsTab.visibility = View.GONE
            }else {
                preApprovalRequestResponseList.removeAt(position)
                preApprovalAdapter.notifyDataSetChanged()
            }
        }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (preApprovalRequestResponseList.isNullOrEmpty()){
                tvNoDataFound.visibility = View.VISIBLE
                llPre_ApprovalItemsTab.visibility = View.GONE
            }
            // showShortToast(it)
        }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                progressBarPB.show()
            } else {
                progressBarPB.dismiss()
            }
        })

    }
    fun removeitem(preApprRequestID: String?) {
        pref = SharedPref(requireContext())
        val preApprovalCancelbody = PreApprovalCancelRequestBody(preApprRequestID)
        viewModel.preApprovalCancelRequest(preApprovalCancelbody, pref.getString(Constants.TOKEN))
    }

    fun setItemPosition(pos: Int, preApprRequestID: String){
        position = pos
        removeitem(preApprRequestID)
    }
    fun lineceNowFragment(publicKey: String, pos: Int) {
        val fragment = LicenceFragment()
        val bundle = Bundle()
        bundle.putString("publickey", publicKey)
        bundle.putString("productNo", preApprovalRequestResponseList[pos].productNo)
        bundle.putString("productTypeID", preApprovalRequestResponseList[pos].productNo)
        bundle.putSerializable("mapId", preApprovalRequestResponseList[pos].productNo)
        fragment.setArguments(bundle)

        addNewFragment(fragment, R.id.container, true)
        MainActivity.mainActivity.bottomNav.visibility = View.GONE

        isBackPreApproval = true
    }


    fun refreshApi(){
        viewModel.preApprovalRequest(pref.getString(Constants.TOKEN))
    }

}