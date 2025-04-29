package com.myoutdoor.agent.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MyViewModal : ViewModel() {

    var apiError= MutableLiveData<String>()
    var isLoading= MutableLiveData<Boolean>()


}