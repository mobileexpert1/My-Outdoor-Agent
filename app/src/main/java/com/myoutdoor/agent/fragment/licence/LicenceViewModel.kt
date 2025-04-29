package com.myoutdoor.agent.fragment.licence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.fragment.licence.model.LicenceKeyBody
import com.myoutdoor.agent.fragment.licence.model.LicenceResponseData
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassBody
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassRequest
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassResponse
import com.myoutdoor.agent.models.freeaccess.FreeAccessBody
import com.myoutdoor.agent.models.freeaccess.response.FreeAccessResponse
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenResponse
import com.myoutdoor.agent.models.licence.newModel.response.ActivityDetailV3Response
import com.myoutdoor.agent.models.licensedetails.formylicense.MyLicenseDetailV2Body
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingResponse
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractResponse
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse
import com.myoutdoor.agent.models.preapprovalrequest.request.PreApprovalRequest
import com.myoutdoor.agent.models.preapprovalrequest.request.PreApprovalRequestResponse
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryResponse
import com.myoutdoor.agent.models.simple_polygen_layer.SimplePolygenResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class LicenceViewModel : BaseViewModel() {


    var rightOfEntryResponseSuccess = MutableLiveData<RightOfEntryResponse>()
    var preApprovalRequestSuccess = MutableLiveData<PreApprovalRequestResponse>()
    var cancelRequestSuccess = MutableLiveData<PreApprovalCancelRequestResponse>()
    var dayPassSuccess = MutableLiveData<DayPassResponse>()
    var getPaymentTokenResponse = MutableLiveData<GetPaymentTokenResponse>()
//    var licenceSuccess = MutableLiveData<LicenceResponseData>()
    var licenceSuccess = MutableLiveData<ActivityDetailV3Response>()
    var freeAccessSuccess = MutableLiveData<FreeAccessResponse>()
    var addHarvestingResponse = MutableLiveData<AddHarvestingResponse>()
    var generateContractResponse = MutableLiveData<GenerateContractResponse>()

    fun rightOfEntryRequest(rightOfEntryBody: RightOfEntryBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.rightOfEntryRequest(
                        rightOfEntryBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    rightOfEntryResponseSuccess.value = response.data!!
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


    fun licenceDetails(licenceKeyBody: LicenceKeyBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.licenceRequest(licenceKeyBody)
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    licenceSuccess.value = response.data!!
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


    fun freeaccess(freeAccessBody: FreeAccessBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.freeAccessRequest(freeAccessBody)
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    freeAccessSuccess.value = response.data!!
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

    var onParticularPolygenLayerSuccess = MutableLiveData<SimplePolygenResponse>()
    var particularPolygonFourLayerSuccess = MutableLiveData<SimplePolygenResponse>()

    fun particluarPolygenLayer(where:String,outFields:String,spatialRel:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.simplePolygonLayer(where,outFields,spatialRel,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                onParticularPolygenLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun particularPolygonFourLayer(where:String,outFields:String,spatialRel:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!


                var response = ResponseHandler().handleSuccess(
                    apiClient.particularPolygonFourLayer(where,outFields,spatialRel,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                particularPolygonFourLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
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
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun getPaymentToken(getPaymentTokenBody: GetPaymentTokenBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getPaymentToken(
                        getPaymentTokenBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    getPaymentTokenResponse.value = response.data!!
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


    fun dayPassAvailibilityRequest(model: DayPassRequest, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.dayPassAvailibilityRequest(
                        model
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    dayPassSuccess.value = response.data!!
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


    fun preapprovalrequestv2(model: PreApprovalRequest, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.preapprovalrequestv2(
                        model
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    preApprovalRequestSuccess.value = response.data!!
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



    ///   add harvesting request



    fun addHarvestingRequest(addHarvestingRequest: AddHarvestingRequest, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.addHarvestingRequest(
                        addHarvestingRequest
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    addHarvestingResponse.value = response.data!!
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



    // Generate Contract pdf


    fun generateContractRequest(generateContractRequest: GenerateContractRequest, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.generateContractv2Request(
                        generateContractRequest
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    generateContractResponse.value = response.data!!
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