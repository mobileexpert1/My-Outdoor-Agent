package com.myoutdoor.agent.fragment

import android.view.View
import com.myoutdoor.agent.ListYourPropertyNewFragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.toolbar.*

class HelpFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_help
    }

    override fun onCreateView() {
        tvToolbar.setText("Help")
//        backpress button
        ivBackpress.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
            requireActivity().onBackPressed()
        }
        llHelpLandOwners.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE

            //val intent = Intent(activity, BasicPlanActivity::class.java)
           // startActivity(intent)
            addNewFragment(ListYourPropertyNewFragment(progressBarPB),R.id.container,true)
           progressBarPB.show()
        }

        llHelpAdventureSeeker.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(FrequentlyAskedQuestionsFragment(),R.id.container,true)
        }
        fragmentBackPressHandle()


    }




}