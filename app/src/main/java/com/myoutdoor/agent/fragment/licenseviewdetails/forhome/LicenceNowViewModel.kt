package com.myoutdoor.agent.fragment.licenseviewdetails.forhome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.licensedetails.forhome.LicenceViewDetailHomeBody
import com.myoutdoor.agent.models.licensedetails.forhome.LicenseNowViewDetailResponse
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class LicenceNowViewModel: BaseViewModel() {

    var licenseViewDetailsResponseSuccess = MutableLiveData<LicenseNowViewDetailResponse>()
    var rightOfEntryResponseSuccess = MutableLiveData<RightOfEntryResponse>()
    var cancelRequestSuccess = MutableLiveData<PreApprovalCancelRequestResponse>()


    fun rightOfEntryRequest(rightOfEntryBody: RightOfEntryBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.rightOfEntryRequest(
                        rightOfEntryBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    rightOfEntryResponseSuccess.value = response.data!!
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


    fun licenseViewDetailsRequest(licenseViewDetailsBody: LicenceViewDetailHomeBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.licenseViewDetailsRequest(
                        licenseViewDetailsBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    licenseViewDetailsResponseSuccess.value = response.data!!
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


    fun preApprovalCancelRequest(preApprovalCancelRequestBody: PreApprovalCancelRequestBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.preApprovalCancelRequest(
                        preApprovalCancelRequestBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    cancelRequestSuccess.value = response.data!!
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