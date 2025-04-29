package com.myoutdoor.agent.models.DayPassAvailibility

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class DayPassAvailibilityViewModel : BaseViewModel() {

    var dayPassSuccess = MutableLiveData<DayPassResponse>()





}