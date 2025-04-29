package com.myoutdoor.agent.fragment.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.fragment.verification.code.response.ForgotPasswordResponse
import com.myoutdoor.agent.models.ForgotPassword.ForgotPasswordBody
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseData
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class ForgotPasswordViewModel:BaseViewModel() {

    var forgotSuccess = MutableLiveData<ForgotPasswordResponse>()

    fun forgotRequest(forgotPasswordBody: ForgotPasswordBody) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.forgotRequest(forgotPasswordBody))
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    forgotSuccess.value = response.data!!
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