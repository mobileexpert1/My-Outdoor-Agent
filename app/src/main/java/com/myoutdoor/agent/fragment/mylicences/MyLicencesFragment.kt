package com.myoutdoor.agent.fragment.mylicences

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.barteksc.pdfviewer.PDFView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.HorizontalAdapter
import com.myoutdoor.agent.adapter.MyLicencesAdapter
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.LicenceAnotherFragment
import com.myoutdoor.agent.models.accept_license.requestbody.AcceptLicenseBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractRequest
import com.myoutdoor.agent.models.mylicences.activelicences.Model
import com.myoutdoor.agent.models.mylicences.expiredlicences.ExpiredLicencesModel
import com.myoutdoor.agent.models.mylicences.memberoflicences.MemberModel
import com.myoutdoor.agent.models.mylicences.pendinglicences.PendingModel
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_my_licences.*
import kotlinx.android.synthetic.main.toolbar.*


class MyLicencesFragment : BaseFragment(),MyLicencesAdapter.OnItemClickListener,HorizontalAdapter.OnItemClickListener {

    lateinit var viewModel: MyLicencesViewModel
    lateinit var pref: SharedPref
    lateinit var myLicencesResponseList: ArrayList<Model>
    lateinit var memberOfLicencesResponseList: ArrayList<MemberModel>
    var testList= ArrayList<Any>()
    lateinit var pendingInvitesLicencesResponseList: ArrayList<PendingModel>
    lateinit var expiredLicencesResponseList: ArrayList<ExpiredLicencesModel>
    lateinit var myLicencesAdapter : MyLicencesAdapter
    val bundle = Bundle()
    var horizontalPosition=0

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_licences
    }

    override fun onCreateView() {
        pref= SharedPref(requireContext())
        showDialogBox = Dialog(requireContext())
        viewModel = ViewModelProvider(this).get(MyLicencesViewModel::class.java)
        myLicencesResponseList = ArrayList()
        memberOfLicencesResponseList = ArrayList()
        pendingInvitesLicencesResponseList = ArrayList()
        expiredLicencesResponseList = ArrayList()

        tvToolbar.setText("My Active License(s)")
//        backpress button
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
        }


        setAdapter(testList=testList)

        byDefaultActive()


        tvActive.setOnClickListener {
            byDefaultActive()

            //Pervious code
//            pref.setBoolean(Constants.IS_ACTIVE,true)
//            pref.setBoolean(Constants.IS_MEMBER,false)
//            pref.setBoolean(Constants.IS_PENDING,false)
//            pref.setBoolean(Constants.IS_EXPIRED,false)

            pref.setBoolean(Constants.IS_ACTIVE,true)
            pref.setBoolean(Constants.IS_MEMBER,true)
            pref.setBoolean(Constants.IS_PENDING,true)
            pref.setBoolean(Constants.IS_EXPIRED,false)
            tvNoDataFound.visibility = View.GONE
            memberOfLicencesResponseList.clear()
            pendingInvitesLicencesResponseList.clear()
            expiredLicencesResponseList.clear()
            myLicencesResponseList.clear()
            viewModel.mylicensesRequest(pref.getString(Constants.TOKEN))

        }

        tvExpired.setOnClickListener {

            tvExpired.setBackgroundResource(R.drawable.login_register_shape_green)
            tvExpired.setTextColor(Color.parseColor("#ffffff"))

            tvActive.setBackgroundResource(R.drawable.my_licences_white_button)
            tvActive.setTextColor(Color.parseColor("#FF000000"))

            pref.setBoolean(Constants.IS_ACTIVE,false)
            pref.setBoolean(Constants.IS_MEMBER,false)
            pref.setBoolean(Constants.IS_PENDING,false)
            pref.setBoolean(Constants.IS_EXPIRED,true)
            tvNoDataFound.visibility = View.GONE
            memberOfLicencesResponseList.clear()
            pendingInvitesLicencesResponseList.clear()
            expiredLicencesResponseList.clear()
            myLicencesResponseList.clear()
            viewModel.expiredLicencesRequest(pref.getString(Constants.TOKEN))
        }

        fragmentBackPressHandle()

        setObserver()

        pref.setBoolean(Constants.IS_ACTIVE,true)
        pref.setBoolean(Constants.IS_MEMBER,true)
        pref.setBoolean(Constants.IS_PENDING,true)
        pref.setBoolean(Constants.IS_EXPIRED,false)
        tvNoDataFound.visibility = View.GONE
        memberOfLicencesResponseList.clear()
        pendingInvitesLicencesResponseList.clear()
        expiredLicencesResponseList.clear()
        myLicencesResponseList.clear()
        viewModel.mylicensesRequest(pref.getString(Constants.TOKEN))

    }


    fun setAdapter(testList: ArrayList<Any>){
        if (testList.isEmpty()){
            gv_items.visibility=View.GONE
            tvNoDataFound.visibility=View.VISIBLE

        }else{
            gv_items.visibility=View.VISIBLE
            tvNoDataFound.visibility=View.GONE
            myLicencesAdapter = MyLicencesAdapter(requireActivity(),testList,
                this@MyLicencesFragment,this)
            { type,data, pos ->
                when(type) {
                    "ACTIVE" -> {
                        var data = data as Model
                        var publickey2=data.publicKey
                        bundle.putString("publickey2", publickey2)
                        Log.e("call","publickey2 Active "+publickey2)
                        val fragment = LicenceAnotherFragment()
                        val bundle = Bundle()
                        bundle.putString("publickey2", publickey2)
                        fragment.setArguments(bundle)
                        addNewFragment(fragment,R.id.container,true)
                    }
                    "MEMBER" -> {
                        var data = data as MemberModel
                        var publickey2=data.publicKey
                        bundle.putString("publickey2", publickey2)
                        Log.e("call","publickey2 Member "+publickey2)
                        val fragment = LicenceAnotherFragment()
                        val bundle = Bundle()
                        bundle.putString("publickey2", publickey2)
                        fragment.setArguments(bundle)
                        addNewFragment(fragment,R.id.container,true)
                    }
                    "PENDING" -> {
                        var data = data as PendingModel
                        acceptAndPayPopup(data)
                    }

                }
//        ACTIVE MEMBER PENDING
            }
            gv_items.adapter = myLicencesAdapter
        }

    }

    var showDialogBox: Dialog? = null

    @SuppressLint("SuspiciousIndentation")
    private fun acceptAndPayPopup(data:PendingModel) {

        showDialogBox!!.setContentView(R.layout.popup_accept_and_pay)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        val lp_ = WindowManager.LayoutParams()
        lp_.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp_.width = WindowManager.LayoutParams.MATCH_PARENT
        lp_.height = WindowManager.LayoutParams.WRAP_CONTENT
        showDialogBox!!.getWindow()!!.setAttributes(lp_)
        var pdfViewAP: PDFView = showDialogBox!!.findViewById<PDFView>(R.id.pdfViewAP)
        var tvAcceptAndPay: TextView =
            showDialogBox!!.findViewById<TextView>(R.id.tvAcceptAndPay)
        tvAcceptAndPay.text= getString(R.string.accept_and_join)
        var ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)
        var progressBar: ProgressBar = showDialogBox!!.findViewById<ProgressBar>(R.id.progressBar)

        var url: String = Constants.AMENITIES_URL+data.licenseAgreement

        Log.e("@@@@", "urllllll     "  + url)

        LicenceFragment.RetrievePDFFromURL(pdfViewAP,progressBar).execute(url)

        tvAcceptAndPay.setOnClickListener {

            var acceptLicenseBody=AcceptLicenseBody(licenseContractID = data.licenseContractID,
                userAccountID =  pref.getString(Constants.userAccountID).toInt() )

            viewModel.acceptLicencesRequest(token = pref.getString(Constants.TOKEN),
                acceptLicenseBody = acceptLicenseBody)

            showDialogBox!!.cancel()
        }

        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }
        showDialogBox!!.show()

    }

    private fun byDefaultActive() {

        tvActive.setBackgroundResource(R.drawable.login_register_shape_green)
        tvActive.setTextColor(Color.parseColor("#ffffff"))

//        tvMember.setBackgroundResource(R.drawable.my_licences_white_button)
//        tvMember.setTextColor(Color.parseColor("#FF000000"))
//        tvPending.setBackgroundResource(R.drawable.my_licences_white_button)
//        tvPending.setTextColor(Color.parseColor("#FF000000"))

        tvExpired.setBackgroundResource(R.drawable.my_licences_white_button)
        tvExpired.setTextColor(Color.parseColor("#FF000000"))

    }




    override fun onClick(index: Int) {
//        horizontalPosition=index
        if(pref.getBoolean(Constants.IS_ACTIVE)==true){
            var publickey2=myLicencesResponseList.get(index).publicKey
            bundle.putString("publickey2", publickey2)
            Log.e("call","publickey2 Active "+publickey2)
            val fragment = LicenceAnotherFragment()
            val bundle = Bundle()
            bundle.putString("publickey2", publickey2)
            fragment.setArguments(bundle)
            addNewFragment(fragment,R.id.container,true)
        } else if (pref.getBoolean(Constants.IS_MEMBER)==true){
            var publickey2=memberOfLicencesResponseList.get(index).publicKey
            bundle.putString("publickey2", publickey2)
            Log.e("call","publickey2 Member "+publickey2)
            val fragment = LicenceAnotherFragment()
            val bundle = Bundle()
            bundle.putString("publickey2", publickey2)
            fragment.setArguments(bundle)
            addNewFragment(fragment,R.id.container,true)
        }

//        replaceFragment(LicenceAnotherFragment(),R.id.container,true)

    }

    fun setObserver() {


        viewModel.myLicencesSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            Log.e("call","list "+it!!.model.toString())

            if (it!!.model.size > 0){
                tvNoDataFound.visibility = View.GONE
            }else{
                tvNoDataFound.visibility = View.VISIBLE
            }

            myLicencesResponseList.clear()
            myLicencesResponseList.addAll(it!!.model)
            testList.clear()
            it!!.model.forEach {
                testList.add(it)
            }

            viewModel.memberOfLicencesRequest(pref.getString(Constants.TOKEN))
        }
        )

        viewModel.memberOfLicencesSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call","list "+it!!.model.toString())

            if (it!!.model.size > 0){
                tvNoDataFound.visibility = View.GONE
            }else{
                tvNoDataFound.visibility = View.GONE
            }

            memberOfLicencesResponseList.clear()
            memberOfLicencesResponseList.addAll(it!!.model)
            it!!.model.forEach {
                testList.add(it)
            }
            viewModel.pendingInvitesLicencesRequest(pref.getString(Constants.TOKEN))

        }
        )

        viewModel.pendingInvitesLicencesSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it!!.model.size > 0){
                tvNoDataFound.visibility = View.GONE
            }else{
                tvNoDataFound.visibility = View.GONE
            }
            Log.e("call","list "+it!!.model.toString())

            pendingInvitesLicencesResponseList.clear()
            pendingInvitesLicencesResponseList.addAll(it!!.model)

            it!!.model.forEach {
                testList.add(it)
            }
            setAdapter(testList = testList)
            gv_items.smoothScrollToPosition(0)
        }
        )

        viewModel.acceptLicencesSuccess.observe(requireActivity(),Observer{
            if (it.message=="Success"){
                testList.clear()
                viewModel.mylicensesRequest(pref.getString(Constants.TOKEN))
            }
        })


        viewModel.expiredLicencesSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it!!.model.size > 0){
                tvNoDataFound.visibility = View.GONE
            }else{
                tvNoDataFound.visibility = View.VISIBLE
            }
            Log.e("call","list "+it!!.model.toString())


            expiredLicencesResponseList.clear()
            expiredLicencesResponseList.addAll(it!!.model)
            testList.clear()
            it!!.model.forEach {
                testList.add(it)
            }

            setAdapter(testList = testList)
            gv_items.smoothScrollToPosition(0)
           }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
          //  showShortToast(it)
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
        })

    }



    override fun onChatListListener(index: Int) {

        Log.e("call","index "+index)
        viewModel = ViewModelProvider(this).get(MyLicencesViewModel::class.java)

        if (index==0){
            pref.setBoolean(Constants.IS_ACTIVE,true)
            pref.setBoolean(Constants.IS_MEMBER,false)
            pref.setBoolean(Constants.IS_PENDING,false)
            pref.setBoolean(Constants.IS_EXPIRED,false)
            tvNoDataFound.visibility = View.GONE
            memberOfLicencesResponseList.clear()
            pendingInvitesLicencesResponseList.clear()
            expiredLicencesResponseList.clear()
            myLicencesResponseList.clear()
            viewModel.mylicensesRequest(pref.getString(Constants.TOKEN))
            viewModel.memberOfLicencesRequest(pref.getString(Constants.TOKEN))
            viewModel.memberOfLicencesRequest(pref.getString(Constants.TOKEN))

//        }else if (index==1){
//            pref.setBoolean(Constants.IS_ACTIVE,false)
//            pref.setBoolean(Constants.IS_MEMBER,true)
//            pref.setBoolean(Constants.IS_PENDING,false)
//            pref.setBoolean(Constants.IS_EXPIRED,false)
//            tvNoDataFound.visibility = View.GONE
//            memberOfLicencesResponseList.clear()
//            pendingInvitesLicencesResponseList.clear()
//            expiredLicencesResponseList.clear()
//            myLicencesResponseList.clear()
//            viewModel.memberOfLicencesRequest(pref.getString(Constants.TOKEN))

//        }else if(index==2){
//            pref.setBoolean(Constants.IS_ACTIVE,false)
//            pref.setBoolean(Constants.IS_MEMBER,false)
//            pref.setBoolean(Constants.IS_PENDING,true)
//            pref.setBoolean(Constants.IS_EXPIRED,false)
//            tvNoDataFound.visibility = View.GONE
//            memberOfLicencesResponseList.clear()
//            pendingInvitesLicencesResponseList.clear()
//            expiredLicencesResponseList.clear()
//            myLicencesResponseList.clear()
////            viewModel.pendingInvitesLicencesRequest(pref.getString(Constants.TOKEN))
//
        }else if (index==1){
            pref.setBoolean(Constants.IS_ACTIVE,false)
            pref.setBoolean(Constants.IS_MEMBER,false)
            pref.setBoolean(Constants.IS_PENDING,false)
            pref.setBoolean(Constants.IS_EXPIRED,true)
            tvNoDataFound.visibility = View.GONE
            memberOfLicencesResponseList.clear()
            pendingInvitesLicencesResponseList.clear()
            expiredLicencesResponseList.clear()
            myLicencesResponseList.clear()
            viewModel.expiredLicencesRequest(pref.getString(Constants.TOKEN))
        }


    }


}