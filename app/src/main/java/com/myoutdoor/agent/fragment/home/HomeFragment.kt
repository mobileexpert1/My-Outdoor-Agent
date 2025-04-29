package com.myoutdoor.agent.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.ListYourPropertyNewFragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.HomeGridAdapter
import com.myoutdoor.agent.adapter.HomeSearchItemAdapter
import com.myoutdoor.agent.adapter.HorizontalAdapter
import com.myoutdoor.agent.adapter.SearchListAdapter
import com.myoutdoor.agent.fragment.FrequentlyAskedQuestionsFragment
import com.myoutdoor.agent.fragment.PreApprovalRequest.PreApprovalRequestFragment
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.home.Model
import com.myoutdoor.agent.models.home.RLURegionModel
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), HorizontalAdapter.OnItemClickListener,
    HomeGridAdapter.OnItemClickListener {

    lateinit var pref: SharedPref
    lateinit var viewModel: HomeViewModel
    private lateinit var homeGridAdapter: HomeGridAdapter
    lateinit var propertyList: ArrayList<Model>
    lateinit var regionListData: ArrayList<RLURegionModel>
    lateinit var data: ArrayList<String>
    lateinit var data2: ArrayList<String>
    lateinit var data3: ArrayList<String>
    var getallamenitiesList = ArrayList<com.myoutdoor.agent.models.getallamenities.Model>()
    var list1 = ArrayList<com.myoutdoor.agent.models.getallamenities.Model>()
    var horizontalPosition = 0
    val bundle = Bundle()
    lateinit var arrayAdapter: ArrayAdapter<String>
    var searchlist = ArrayList<com.myoutdoor.agent.models.savedsearches.searchautofill.Model>()
    var searchListItems: ArrayList<String> = ArrayList()
    private var searchListAdapter: RecyclerView.Adapter<SearchListAdapter.ViewHolder>? = null
    var isAdapterClicked: Boolean = false
    private lateinit var homeSearchItemAdapter: HomeSearchItemAdapter
    var common: String? = null
    var commontype: String? = null
    var selectstate: String=""
    var selectFromList: String? = null
    lateinit var commonarray: ArrayList<String>
    lateinit var amenityarray: ArrayList<String>
    lateinit var dummylist: ArrayList<String>
    lateinit var normal: String
    lateinit var freetextcommon: String

    companion object {
        lateinit var homeFragment: HomeFragment
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onCreateView() {

        pvSearchSelect.spinnerOutsideTouchListener = object : OnSpinnerOutsideTouchListener {
            override fun onSpinnerOutsideTouch(view: View, event: MotionEvent) {
                pvSearchSelect.dismiss()
            }
        }

        homeFragment = this
        pref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        propertyList = ArrayList()
        regionListData = ArrayList()

        rvSpecialforyou.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        data = ArrayList()
        data2 = ArrayList()
        data3 = ArrayList()
        commonarray = ArrayList()
        amenityarray = ArrayList()
        dummylist = ArrayList()
        searchlist.clear()
        searchListItems.clear()

        gvHomeGridItemsTab.layoutManager = GridLayoutManager(context, 2)

        llLandOwners.setOnClickListener {
            pvSearchSelect.dismiss()
            MainActivity.mainActivity.bottomNav.visibility = View.GONE
            progressBarPB.show()
            addNewFragment(ListYourPropertyNewFragment(progressBarPB), R.id.container, true)
        }

        llAdvantureSeekers.setOnClickListener {
            pvSearchSelect.dismiss()
            MainActivity.mainActivity.bottomNav.visibility = View.GONE
            addNewFragment(FrequentlyAskedQuestionsFragment(), R.id.container, true)
        }

        if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
            tvUserName.setText("" + pref.getString(Constants.USER_NAME))
        }else {
            tvUserName.setText(getString(R.string.guest))
        }

        viewModel.getAllAmenitiesRequest(pref.getString(Constants.TOKEN))
        viewModel.regionWisePropertiesRequest(pref.getString(Constants.TOKEN))

        tvHomeSearch.setOnClickListener {
            searchFunctionality()
        }
        setObserver()

    }

    private fun searchFunctionality(){
        pvSearchSelect.dismiss()
        amenityarray.clear()
        commonarray.clear()
        if (selectFromList != null) {

            if (commontype.equals("RLU")) {
                pref.setBoolean(Constants.IS_HOME_API_HIT,true)
                bundlePassToSearchFragmentRLU(selectstate.toString(),selectFromList.toString())
            } else if (commontype.equals("County")) {
                pref.setBoolean(Constants.IS_HOME_API_HIT,true)
                bundlePassToSearchFragmentCounty(selectstate.toString(),selectFromList.toString())
            } else if (commontype.equals("Region")) {
                pref.setBoolean(Constants.IS_HOME_API_HIT,true)
                bundlePassToSearchFragmentRegion(selectstate.toString(),selectFromList.toString())
            } else {
                pref.setBoolean(Constants.IS_HOME_API_HIT,true)
                bundlePassToSearchFragmentElseProperty(selectstate.toString(),selectFromList.toString())
            }
        }
        else {
            normal = autoTextViewHome.text.toString()
            freetextcommon = normal
            normal?.let { commonarray.add(it) }
            pref.setBoolean(Constants.IS_HOME_API_HIT,true)
            bundlePassToSearchFragmentWithFreeText(selectstate.toString(),normal.toString())
        }
    }

    fun bundlePassToSearchFragmentRLU(amenityarray:String,rluList:String){
        val fragment = SearchFragment()
        bundle.putString("amenityarray", amenityarray)
        bundle.putString("rluList", rluList)
        fragment.setArguments(bundle)
        addNewFragment(fragment,R.id.container,true)
    }

    fun bundlePassToSearchFragmentCounty(amenityarray:String,countyList:String){
        val fragment = SearchFragment()
        bundle.putString("amenityarray", amenityarray)
        bundle.putString("countyList", countyList)
        fragment.setArguments(bundle)
        addNewFragment(fragment,R.id.container,true)
    }

    fun bundlePassToSearchFragmentRegion(amenityarray:String,regionList:String){
        val fragment = SearchFragment()
        bundle.putString("amenityarray", amenityarray)
        bundle.putString("regionList", regionList)
        fragment.setArguments(bundle)
        addNewFragment(fragment,R.id.container,true)
    }

    fun bundlePassToSearchFragmentElseProperty(amenityarray:String,propertyList:String){
        val fragment = SearchFragment()
        bundle.putString("amenityarray", amenityarray)
        bundle.putString("propertyList", propertyList)
        fragment.setArguments(bundle)
        addNewFragment(fragment,R.id.container,true)
    }

    fun bundlePassToSearchFragmentWithFreeText(amenityarrayfreet:String,freetext:String){
        val fragment = SearchFragment()
        bundle.putString("amenityarrayfreet", amenityarrayfreet)
        bundle.putString("freetext", freetext)
        fragment.setArguments(bundle)
        addNewFragment(fragment,R.id.container,true)
        MainActivity.mainActivity.bottomNav.getMenu().getItem(1).setChecked(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoTextViewHome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (isAdapterClicked) {
                    isAdapterClicked = false
                } else {
                    val searchAutoFillBody = SearchAutoFillBody(
                        "" + autoTextViewHome.text
                    )
                    viewModel.searchAutoFillRequest(
                        searchAutoFillBody,
                        pref.getString(Constants.TOKEN)
                    )
                }
            }
        })

    }


    override fun onChatListListener(index: Int) {
        horizontalPosition = index

        regionListData.clear()
        regionListData.addAll(propertyList.get(index).rLURegionModel)

        if (regionListData.size > 0) {

            Log.e("call","SECOND TIME regionListData "+regionListData.toString())

            homeGridAdapter =
                HomeGridAdapter(regionListData, context = HomeFragment(), this)
            gvHomeGridItemsTab.adapter = homeGridAdapter

            gvHomeGridItemsTab.visibility = View.VISIBLE
            tvNoDataFound.visibility = View.GONE
         //   homeGridAdapter.notifyAdapter(horizontalPosition)
   //         homeGridAdapter.notifyDataSetChanged()
        } else {
            gvHomeGridItemsTab.visibility = View.GONE
            tvNoDataFound.visibility = View.VISIBLE
        }
    }

    fun setObserver() {

        viewModel.searchAutoFillResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")
            Log.e("call", "list" + it!!.model.toString())
            if (it.message != "Success") {
//             showShortToast(it.message!!)
            } else {

                searchlist.clear()
                searchListItems.clear()
                searchlist.addAll(it!!.model)

                for (i in 0 until searchlist.size) {
                    searchListItems.add(searchlist.get(i).searchResult)
                }

                if (autoTextViewHome.text.toString().equals("")) {
                    searchListItems.clear()
                    rvSearchHome.visibility = View.GONE
                    homeSearchItemAdapter.notifyDataSetChanged()
                } else {
                    if (searchListItems.size > 0) {
                        rvSearchHome.visibility = View.VISIBLE
                        rvSearchHome.layoutManager = LinearLayoutManager(activity)
                        homeSearchItemAdapter =
                            HomeSearchItemAdapter(this@HomeFragment, searchlist)
                        rvSearchHome.adapter = homeSearchItemAdapter
                    } else {
                        searchListItems.clear()
                        rvSearchHome.visibility = View.GONE
                        homeSearchItemAdapter.notifyDataSetChanged()
                    }
                }

                for (i in 0 until searchlist.size) {
                    if (it.model.get(i).type.equals("RLU")) {
//                        commontype = "RLU"
                        common = it.model.get(i).searchResult
                    } else if (it.model.get(i).type.equals("County")) {
//                        commontype = "County"
                        common = it.model.get(i).searchResult
                    } else if (it.model.get(i).type.equals("Region")) {
//                        commontype = "Region"
                        common = it.model.get(i).searchResult
                    } else {
//                        commontype = ""
                        common = it.model.get(i).searchResult
                    }
                    searchListItems.add(searchlist.get(i).searchResult)
                }

            }

        }
        )


        viewModel.getAllAmenitiesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            getallamenitiesList.addAll(it.model)
            for (i in 0 until getallamenitiesList.size) {
                data2.add(getallamenitiesList.get(i).amenityName)
            }
            pvSearchSelect?.setItems(data2!!)
            pvSearchSelect.setIsFocusable(false)

            pvSearchSelect.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                // do something
                selectstate = newItem
            }

        }
        )

        viewModel.searchResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            if (it.message == "Success") {
                /*   replaceFragment
                   val transaction = supportFragmentManager.beginTransaction()
                   transaction.replace(R.id.fragment_layout_id, fragment)
                   transaction.commit()*/
                addNewFragment(SearchFragment(), R.id.container, true)
//            MainActivity.mainActivity.bottomNav.setSelectedItemId(R.id.search);

            }

        }
        )

        viewModel.homeSuccess.observe(requireActivity(), Observer {



            Log.d("@@@@", "Success")
            var index: Int

            data.clear()
            for (item in it!!.model.indices) {
                data.add(it!!.model.get(item).regionName)
            }


            Log.e("call", "234   " + data.toString())
            rvSpecialforyou.adapter = HorizontalAdapter(data, this)

            Log.e("call", "list " + it!!.model.toString())
            propertyList.addAll(it!!.model)
            regionListData.clear()
            regionListData.addAll(propertyList.get(0).rLURegionModel)

            Log.e("call","FIRST TIME regionListData "+regionListData.toString())
            Log.e("call","FIRST TIME propertyList "+propertyList.toString())

            homeGridAdapter =
                HomeGridAdapter(regionListData, context = HomeFragment(), this)
            gvHomeGridItemsTab.adapter = homeGridAdapter

            if (propertyList.size == 0) {
                gvHomeGridItemsTab.visibility = View.GONE
                tvNoDataFound.visibility = View.VISIBLE
            }

        }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "api error Failed::" + it.toString())
        //    showShortToast(it)
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

    override fun onItemClick(index: Int) {
        Log.e("call","@@@ horizontalPosition  "+horizontalPosition)
        pvSearchSelect.dismiss()
        var publickey = propertyList.get(horizontalPosition).rLURegionModel.get(index).publicKey

        bundle.putString("publickey", publickey)
        Log.e("call", "publickey " + publickey)

        val fragment = LicenceFragment()
        val bundle = Bundle()
        bundle.putString("publickey", publickey)

        bundle.putString("productNo", regionListData[index].productNo.toString())
        bundle.putString("productTypeID", regionListData[index].productID.toString())
        bundle.putSerializable("mapId", regionListData[index].productNo)


        fragment.setArguments(bundle)

        addNewFragment(fragment, R.id.container, true)
        MainActivity.mainActivity.bottomNav.visibility = View.GONE

        PreApprovalRequestFragment.isBackPreApproval = false
    }


    public fun searchClickItem(text: String,type:String) {
        isAdapterClicked = true
        autoTextViewHome.setText(text)
        commontype=type
        selectFromList = autoTextViewHome.text.toString()
        rvSearchHome.visibility = View.GONE
        pvSearchSelect.dismiss()
    }


}