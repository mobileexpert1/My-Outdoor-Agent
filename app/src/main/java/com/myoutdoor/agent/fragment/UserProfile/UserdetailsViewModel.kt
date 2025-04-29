package com.myoutdoor.agent.fragment.UserProfile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.changepassword.ChangePasswordBody
import com.myoutdoor.agent.models.changepassword.ChangePasswordResponse
import com.myoutdoor.agent.models.deletesearch.DeleteSearchBody
import com.myoutdoor.agent.models.deletesearch.DeleteSearchResponse
import com.myoutdoor.agent.models.getallstates.GetAllStatesResponse
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsBody
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsResponse
import com.myoutdoor.agent.models.savedsearches.getsavedsearches.GetSavedSearchesResponse
import com.myoutdoor.agent.models.userdetails.EditProfileBody
import com.myoutdoor.agent.models.userdetails.EditProfileResponse
import com.myoutdoor.agent.models.userdetails.deleteruser.DeleteUserResponse
import com.myoutdoor.agent.models.userdetails.getuserdetails.GetUserDetailsResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class UserdetailsViewModel : BaseViewModel() {

    var editProfileResponseSuccess = MutableLiveData<EditProfileResponse>()
    var changePasswordResponseSuccess = MutableLiveData<ChangePasswordResponse>()
    var getAllStatesResponseSuccess = MutableLiveData<GetAllStatesResponse>()
    var getUserDetailsResponseSuccess = MutableLiveData<GetUserDetailsResponse>()
    var manageNotificationsResponseResponseSuccess = MutableLiveData<ManageNotificationsResponse>()
    var getSavedSearchesResponseSuccess = MutableLiveData<GetSavedSearchesResponse>()
    var deleteSearchResponseSuccess = MutableLiveData<DeleteSearchResponse>()
    var deleteUserResponseSuccess = MutableLiveData<DeleteUserResponse>()


    fun deleteUserRequest(token: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.deleteUserRequest()
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    deleteUserResponseSuccess.value = response.data!!
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

    fun deletesearchRequest(deleteSearchBody: DeleteSearchBody, token: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.deletesearchRequest(
                        deleteSearchBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    deleteSearchResponseSuccess.value = response.data!!
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


    fun getSavedSearchesRequest(token: String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getSavedSearchesRequest()
                )

                isLoading.value = false

                Log.e("call", "DATA " + response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    getSavedSearchesResponseSuccess.value = data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                Log.e("call", "ass " + e.toString())
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun userDetailsRequest(token: String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.userDetailsRequest()
                )

                isLoading.value = false

                Log.e("call", "DATA " + response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    getUserDetailsResponseSuccess.value = data!!
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

    fun getAllStatesRequest(token: String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAllStatesRequest()
                )

                isLoading.value = false

                Log.e("call", "DATA " + response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    getAllStatesResponseSuccess.value = data!!
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


    fun editProfileRequest(editProfileBody: EditProfileBody, token: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.editProfileRequest(
                        editProfileBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    editProfileResponseSuccess.value = response.data!!
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

    fun manageNotificationRequest(manageNotificationsBody: ManageNotificationsBody, token: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.manageNotificationRequest(
                        manageNotificationsBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    manageNotificationsResponseResponseSuccess.value = response.data!!
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


    fun changePasswordRequest(changePasswordBody: ChangePasswordBody, token: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.changePasswordRequest(
                        changePasswordBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    changePasswordResponseSuccess.value = response.data!!
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