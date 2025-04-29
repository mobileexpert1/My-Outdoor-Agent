package com.myoutdoor.agent.fragment.mylicences

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.accept_license.requestbody.AcceptLicenseBody
import com.myoutdoor.agent.models.accept_license.response.AcceptLicenseResponse
import com.myoutdoor.agent.models.mylicences.activelicences.ActiveLicencesResponse
import com.myoutdoor.agent.models.mylicences.expiredlicences.ExpiredLicencesResponse
import com.myoutdoor.agent.models.mylicences.memberoflicences.MemberOfLicencesResponse
import com.myoutdoor.agent.models.mylicences.pendinglicences.PendingInvitesLicencesResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class MyLicencesViewModel: BaseViewModel() {

    var myLicencesSuccess = MutableLiveData<ActiveLicencesResponse>()
    var memberOfLicencesSuccess = MutableLiveData<MemberOfLicencesResponse>()
    var pendingInvitesLicencesSuccess = MutableLiveData<PendingInvitesLicencesResponse>()
    var expiredLicencesSuccess = MutableLiveData<ExpiredLicencesResponse>()
    var acceptLicencesSuccess = MutableLiveData<AcceptLicenseResponse>()

    // my licences api

    fun mylicensesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.mylicenses()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    myLicencesSuccess.value = data!!
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

    fun memberOfLicencesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.memberOfLicencesRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    memberOfLicencesSuccess.value = data!!
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

    fun pendingInvitesLicencesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.pendinginviteslicensesRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    pendingInvitesLicencesSuccess.value = data!!
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

    fun expiredLicencesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.expiredLicencesRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    expiredLicencesSuccess.value = data!!
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

    fun acceptLicencesRequest(token:String,acceptLicenseBody: AcceptLicenseBody) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.acceptlicenseRequest(acceptLicenseBody)
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    acceptLicencesSuccess.value = data!!
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