package com.myoutdoor.agent.fragment.PreApprovalRequest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.preapprovalrequest.PreRequestResponse
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse

import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class PreApprovalRequestViewModel: BaseViewModel() {

    var preApprovalSuccess = MutableLiveData<PreRequestResponse>()
    var cancelRequestSuccess = MutableLiveData<PreApprovalCancelRequestResponse>()

    fun preApprovalRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.preApprovalRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    preApprovalSuccess.value = data!!
                } else {
                    apiError.value = response.message
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
         //       apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }
}