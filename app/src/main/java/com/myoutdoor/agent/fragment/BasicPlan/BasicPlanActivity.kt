package com.myoutdoor.agent.fragment.BasicPlan

import androidx.fragment.app.Fragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.HelpFragment
import com.myoutdoor.agent.fragment.home.HomeFragment
import com.myoutdoor.agent.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_basic_plan.*
import kotlinx.android.synthetic.main.toolbar.*

class BasicPlanActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return  R.layout.activity_basic_plan
    }



    override fun onLayoutCreated() {

        tvToolbar.setText("Choose your plan")
        ivBackpress.setOnClickListener {
            onBackPressed()
        }


        add_NewFragment(ChooseYourPlanFragment(),R.id.FLBasiccontainer,false)
        tvProPlan.setOnClickListener {
            replace_Fragment(ProPlanFragment(),R.id.FLBasiccontainer,false)
        }

        tvBasicPlan.setOnClickListener {
            replace_Fragment(ChooseYourPlanFragment(),R.id.FLBasiccontainer,false)
        }

        tvEnterpricePlan.setOnClickListener {
            replace_Fragment(EnterprisePlanFragment(),R.id.FLBasiccontainer,false)
        }


    }


    fun add_NewFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.add(container, fragment, tag).commitAllowingStateLoss()

    }

    fun replace_Fragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(container, fragment, tag).commit()
    }



}