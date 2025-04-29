package com.myoutdoor.agent.fragment.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.message.MessageResponse
import com.myoutdoor.agent.models.preapprovalrequest.PreRequestResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class MessageViewModel: BaseViewModel() {

    var messageSuccess = MutableLiveData<MessageResponse>()
    fun messageRequest(token:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.messageRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    messageSuccess.value = data!!
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