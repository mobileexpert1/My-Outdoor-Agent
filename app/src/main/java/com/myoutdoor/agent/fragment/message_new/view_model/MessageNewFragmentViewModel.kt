package com.myoutdoor.agent.fragment.message_new.view_model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.fragment.licence.model.LicenceKeyBody
import com.myoutdoor.agent.fragment.licence.model.LicenceResponseData
import com.myoutdoor.agent.fragment.message_new.model.get_messages.GetAllMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.get_messages.GetAllMessagesResponse
import com.myoutdoor.agent.fragment.message_new.model.refresh_messages.RefreshMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.refresh_messages.RefreshMessagesResponse
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageRequest
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageResponse
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassBody
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassRequest
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassResponse
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenResponse
import com.myoutdoor.agent.models.licensedetails.formylicense.MyLicenseDetailV2Body
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class MessageNewFragmentViewModel : BaseViewModel() {


    var getAllMessagesResponseSuccess = MutableLiveData<GetAllMessagesResponse>()
    var refreshMessagesResponseSuccess = MutableLiveData<RefreshMessagesResponse>()


    fun getAllMessagesRequest(getAllMessagesRequest: GetAllMessagesRequest, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAllMessages(
                        getAllMessagesRequest
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    getAllMessagesResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
           //     apiError.value = exception.message.toString()
                isLoading.value = false
            }
        }
    }

    fun refreshMessagesRequest(refreshMessagesRequest: RefreshMessagesRequest, token:String, isLoader:Boolean){

        viewModelScope.launch {
            if (isLoader){
                isLoading.value = true
            }else {
                isLoading.value = false
            }

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.refreshMessages(
                        refreshMessagesRequest
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    refreshMessagesResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
              //  apiError.value = exception.message.toString()
                isLoading.value = false
            }
        }
    }


}