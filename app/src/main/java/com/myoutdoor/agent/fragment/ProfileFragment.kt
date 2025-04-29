package com.myoutdoor.agent.fragment

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myoutdoor.agent.PrivacyPolicyFragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.fragment.PreApprovalRequest.PreApprovalRequestFragment
import com.myoutdoor.agent.fragment.UserProfile.MyAccountFragment
import com.myoutdoor.agent.fragment.contactus.ContactUsFragment
import com.myoutdoor.agent.fragment.mylicences.MyLicencesFragment
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var pref: SharedPref

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onCreateView() {
//        lateinit var view: View
//        bottomNav = view.findViewById(R.id.bottomNav) as BottomNavigationView
        pref= SharedPref(requireContext())

        tvProfileName.setText(pref.getString(Constants.USER_NAME))

        llMyAccount.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(MyAccountFragment(),R.id.container,true)
        }

        llMyLicences.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(MyLicencesFragment(),R.id.container,true)

        }

        llPre_Approval.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(PreApprovalRequestFragment(),R.id.container,true)
        }

        llHelp.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(HelpFragment(),R.id.container,true)
        }

        llContactUs.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(ContactUsFragment(),R.id.container,true)
        }

        llPrivacyPolicy.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility= View.GONE
            addNewFragment(PrivacyPolicyFragment(progressBarPB),R.id.container,true)
              progressBarPB.show()

        }

    }
}