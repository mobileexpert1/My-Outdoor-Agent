package com.myoutdoor.agent.fragment.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.getallamenities.GetAllAmenitiesResponse
import com.myoutdoor.agent.models.home.HomeResponse
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillBody
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillResponse
import com.myoutdoor.agent.models.search.SearchBody
import com.myoutdoor.agent.models.search.SearchResponse
import com.myoutdoor.agent.models.search.searchV2.SearchV2Response
import com.myoutdoor.agent.models.search.searchV2.body.SearchV2Body
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel: BaseViewModel() {

    var homeSuccess = MutableLiveData<HomeResponse>()
    var searchAutoFillResponseSuccess = MutableLiveData<SearchAutoFillResponse>()
//    var searchResponseSuccess = MutableLiveData<SearchResponse>()
    var searchResponseSuccess = MutableLiveData<SearchV2Response>()
    var getAllAmenitiesResponseSuccess = MutableLiveData<GetAllAmenitiesResponse>()

    fun getAllAmenitiesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAllAmenitiesRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    getAllAmenitiesResponseSuccess.value = data!!
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

    fun searchRequest(searchBody: SearchV2Body, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.searchRequest(
                        searchBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    searchResponseSuccess.value = response.data!!
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
    fun searchAutoFillRequest(searchAutoFillBody: SearchAutoFillBody, token:String){

        viewModelScope.launch {
            //  isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.searchAutoFillRequest(
                        searchAutoFillBody
                    )
                )
                //  isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    searchAutoFillResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            }
            catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)


                if (exception.message.toString().equals("Something went wrong")){

                }
                else{
                    apiError.value = exception.message.toString()

                }
                isLoading.value = false


            //    apiError.value = exception.message.toString()
                //  isLoading.value = false
            }

        }

    }


    fun regionWisePropertiesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.regionwiseproperties()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    homeSuccess.value = data!!
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