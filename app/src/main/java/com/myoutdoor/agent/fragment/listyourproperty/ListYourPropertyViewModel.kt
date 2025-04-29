package com.myoutdoor.agent.fragment.listyourproperty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.contactus.ContactUsBody
import com.myoutdoor.agent.models.contactus.ContactUsResponse
import com.myoutdoor.agent.models.listyourproperty.ListYourPropertyBody
import com.myoutdoor.agent.models.listyourproperty.ListYourPropertyResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class ListYourPropertyViewModel: BaseViewModel() {

    var listYourPropertyResponseSuccess = MutableLiveData<ListYourPropertyResponse>()

    fun listYourPropertyRequest(listYourPropertyBody: ListYourPropertyBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.listYourPropertyRequest(
                        listYourPropertyBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    listYourPropertyResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }



}