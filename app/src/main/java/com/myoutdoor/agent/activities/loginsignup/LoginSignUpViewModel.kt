package com.myoutdoor.agent.activities.loginsignup

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.register.LoginBody
import com.myoutdoor.agent.models.register.LoginResponse
import com.myoutdoor.agent.models.register.RegisterBody
import com.myoutdoor.agent.models.register.RegisterResponse

import com.myoutdoor.agent.models.sociallogin.SocailLoginBody
import com.myoutdoor.agent.models.sociallogin.SocailLoginResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseData
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class LoginSignUpViewModel : BaseViewModel() {

    var registerSuccess = MutableLiveData<BaseData<RegisterResponse>>()
    var loginSuccess = MutableLiveData<BaseData<LoginResponse>>()
    var signupResponseSuccess = MutableLiveData<SocailLoginResponse>()
    var apiLoginError= MutableLiveData<String>()

    fun signupRequest(signupBody: SocailLoginBody) {

        viewModelScope.launch {
            //  isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.signupRequest(
                        signupBody
                    )
                )
                //  isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    signupResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                //  isLoading.value = false
            }

        }

    }

         fun registerRequest(
        registerBody: RegisterBody
    ) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.registerRequest(
                        registerBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    registerSuccess.value = response.data!!
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



        fun loginRequest(loginBody: LoginBody)
        {
            viewModelScope.launch {
                isLoading.value = true
                try {
                    var apiClient = ApiClient.getApiClient()!!
                    var response = ResponseHandler().handleSuccess(
                        apiClient.loginRequest(
                            loginBody
                        )
                    )
                    isLoading.value = false

                    var data = response.data

                    if (data!!.statusCode == 200) {
                        loginSuccess.value = response.data!!
                    } else {
                        apiError.value = data.message
                    }

                } catch (e: Exception) {
                    var exception = ResponseHandler().handleException<String>(e)
                   // apiError.value = exception.message.toString()
                    apiLoginError.value = "Account does not exist with this email."
                    isLoading.value = false
                }

            }
        }




}