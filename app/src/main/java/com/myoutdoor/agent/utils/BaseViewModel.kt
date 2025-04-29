package com.myoutdoor.agent.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageRequest
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

open class BaseViewModel : ViewModel() {

    var apiError= MutableLiveData<String>()
    var isLoading= MutableLiveData<Boolean>()
    var sendMessageResponseSuccess = MutableLiveData<SendMessageResponse>()


//    fun sendMessagesRequest(sendMessageRequest: SendMessageRequest, token:String){
    fun sendMessagesRequest(MessageText: String, ProductID: Int,token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithCHeader(token)!!

                val messageText = RequestBody.create(MultipartBody.FORM, MessageText)
                val productID = RequestBody.create(MultipartBody.FORM, ProductID.toString())

                var response = ResponseHandler().handleSuccess(
                    apiClient.sendMessage(messageText,productID)
                )
                isLoading.value = false

                if (response.data!!.statusCode == 200) {
                    var data = response.data
                    sendMessageResponseSuccess.value = response.data!!
                } else {
                    apiError.value = response.data!!.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
              //  apiError.value = exception.message.toString()
                isLoading.value = false
            }
        }
    }

}