package com.myoutdoor.agent.fragment.contactus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.contactus.ContactUsBody
import com.myoutdoor.agent.models.contactus.ContactUsResponse
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class ContactUsViewModel: BaseViewModel() {

    var contactUsSuccess = MutableLiveData<ContactUsResponse>()

    fun contactUsRequest(contactUsBody: ContactUsBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.contactUsRequest(
                        contactUsBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    contactUsSuccess.value = response.data!!
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