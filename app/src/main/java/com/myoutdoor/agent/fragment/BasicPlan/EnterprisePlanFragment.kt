package com.myoutdoor.agent.fragment.BasicPlan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.HelpFragment
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*

class EnterprisePlanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enterprise_plan, container, false)
    }


}