package com.myoutdoor.agent.activities



import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.*
import com.myoutdoor.agent.fragment.home.HomeFragment
import com.myoutdoor.agent.fragment.message.PropertyListFragment
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.utils.BaseActivity
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref

class MainActivity : BaseActivity() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var pref: SharedPref

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    companion object {
        lateinit var mainActivity:MainActivity
    }

    override fun onLayoutCreated() {

        mainActivity = this
        pref = SharedPref(this)

       // addNewFragment(HomeFragment(),R.id.container,false)
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.visibility=View.VISIBLE
        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    addNewFragment(HomeFragment(),R.id.container,false)
                }

                R.id.message-> {
                    if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
                        addNewFragment(PropertyListFragment(),R.id.container,false)
                    }else {
                        addNewFragment(PropertyListFragment(),R.id.container,false)
                        guestAlertMessage()
                    }
                }

                R.id.search->{
                    addNewFragment(SearchFragment(),R.id.container,false)
                }

                R.id.profile-> {
                    if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
                        addNewFragment(ProfileFragment(),R.id.container,false)
                    }else {
                        addNewFragment(ProfileFragment(),R.id.container,false)
                        guestAlertMessage()
                    }
                }
            }
            true
        }

    }




}