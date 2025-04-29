package com.myoutdoor.agent.fragment.verification.code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.fragment.verification.code.body.SendVerificationBody
import com.myoutdoor.agent.fragment.verification.code.body.UpdatePasswordBody
import com.myoutdoor.agent.fragment.verification.code.body.VerifyCodeBody
import com.myoutdoor.agent.fragment.verification.code.response.UpdatePasswordResponse
import com.myoutdoor.agent.models.verification.SendVerificationCodeResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class VerificationViewModel: BaseViewModel() {

    var sendVerificationCodeResponse = MutableLiveData<SendVerificationCodeResponse>()
    var verifyCodeResponse = MutableLiveData<SendVerificationCodeResponse>()
    var updatePasswordResponse = MutableLiveData<UpdatePasswordResponse>()

    fun sendVerificationRequest(
        sendVerificationBody: SendVerificationBody
    ) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.sendverificationcodeRequest(sendVerificationBody))
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    sendVerificationCodeResponse.value = response.data!!
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

    fun verifyCodeRequest(
        sendVerificationBody: VerifyCodeBody
    ) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.verifyCodeRequest(sendVerificationBody))
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    verifyCodeResponse.value = response.data!!
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


    fun updatePasswordRequest(
        updatePasswordBody: UpdatePasswordBody
    ) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.updatepasswordRequest(updatePasswordBody))
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    updatePasswordResponse.value = response.data!!
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