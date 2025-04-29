package com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.addclubmember.AddClubMemberBody
import com.myoutdoor.agent.models.addclubmember.AddClubMemberResponse
import com.myoutdoor.agent.models.addupdatevehicle.AddUpdateVehicleBody
import com.myoutdoor.agent.models.addupdatevehicle.AddUpdateVehicleResponse
import com.myoutdoor.agent.models.clubmemberdetails.ClubMemberDetailsBody
import com.myoutdoor.agent.models.clubmemberdetails.ClubMemberDetailsResponse
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenResponse
import com.myoutdoor.agent.models.getallstates.GetAllStatesResponse
import com.myoutdoor.agent.models.invitemember.InviteMemberBody
import com.myoutdoor.agent.models.invitemember.InviteMemberResponse
import com.myoutdoor.agent.models.licensedetails.formylicense.license.MyDetailsV2Body
import com.myoutdoor.agent.models.licensedetails.formylicense.license.MyDetailsV2Response
import com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.LicenceDetailV3Response
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingResponse
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractResponse
import com.myoutdoor.agent.models.memberRemove.MemberRemoveBody
import com.myoutdoor.agent.models.memberRemove.MemberRemoveResponse
import com.myoutdoor.agent.models.propertylicense.PropertyLicenseBody
import com.myoutdoor.agent.models.propertylicense.PropertyLicenseResponse
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryResponse
import com.myoutdoor.agent.models.vehicleremove.VehicleRemoveBody
import com.myoutdoor.agent.models.vehicleremove.VehicleRemoveResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import kotlinx.coroutines.launch

class MyLicenseDetailV2ViewModel: BaseViewModel()  {

  var myLicenseDetailV2ResponseSuccess = MutableLiveData<MyDetailsV2Response>()
//    var myLicenseDetailV2ResponseSuccess = MutableLiveData<LicenceDetailV3Response>()
    var addUpdateVehicleResponseSuccess = MutableLiveData<AddUpdateVehicleResponse>()
    var clubMemberDetailsResponseSuccess = MutableLiveData<ClubMemberDetailsResponse>()
    var propertyLicenseResponseSuccess = MutableLiveData<PropertyLicenseResponse>()
    var vehicleRemoveResponsecSuccess = MutableLiveData<VehicleRemoveResponse>()
    var inviteMemberResponsecSuccess = MutableLiveData<InviteMemberResponse>()
    var memberRemoveResponsecSuccess = MutableLiveData<MemberRemoveResponse>()
    var getAllStatesResponseSuccess = MutableLiveData<GetAllStatesResponse>()
    var addClubMemberResponseSuccess = MutableLiveData<AddClubMemberResponse>()
    var getPaymentTokenResponse = MutableLiveData<GetPaymentTokenResponse>()
    var apiPropertyLicenseError= MutableLiveData<String>()

    // add club member api

    fun addClubMemberRequest(addClubMemberBody: AddClubMemberBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.addClubRequest(
                        addClubMemberBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    addClubMemberResponseSuccess.value = response.data!!
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


    fun memberRemoveRequest(memberRemoveBody: MemberRemoveBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.removeMemberRequest(
                        memberRemoveBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    memberRemoveResponsecSuccess.value = response.data!!
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

    fun inviteMemberRequest(inviteMemberBody: InviteMemberBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.inviteMemberRequest(
                        inviteMemberBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    inviteMemberResponsecSuccess.value = response.data!!
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
    fun vehicleRemoveRequest(vehicleRemoveBody: VehicleRemoveBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.vehicleRemoveRequest(
                        vehicleRemoveBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    vehicleRemoveResponsecSuccess.value = response.data!!
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

    fun propertyLicenseRequest(propertyLicenseBody: PropertyLicenseBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.propertyLicenseRequest(
                        propertyLicenseBody
                    )
                )

                isLoading.value = false

                try {
                    var data = response.data
                    if (data!!.statusCode == 200) {
                        propertyLicenseResponseSuccess.value = response.data!!
                    } else {
                        apiError.value = data.message
                    }

                }catch (e:java.lang.Exception){
                    Log.e("call","exception: "+e.toString())
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                Log.e("call","exception: "+exception.toString())
                apiPropertyLicenseError.value = "Cannot access Permit Agreement - Missing vehicle info."
                isLoading.value = false
            }

        }

    }

    fun clubMemberDetailsRequest(clubMemberDetailsBody: ClubMemberDetailsBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.clubMemberDetailsRequest(
                        clubMemberDetailsBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    clubMemberDetailsResponseSuccess.value = response.data!!
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

    fun addUpdateVehicleRequest(addUpdateVehicleBody: AddUpdateVehicleBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.addUpdateVehicleRequest(
                        addUpdateVehicleBody
                    )
                )

                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    addUpdateVehicleResponseSuccess.value = response.data!!
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

    fun myLicenseDetailV2Request(myLicenseDetailV2Body: MyDetailsV2Body, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.myLicenseDetailV2Request(
                        myLicenseDetailV2Body
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    myLicenseDetailV2ResponseSuccess.value = response.data!!
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

    fun getAllStatesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAllStatesRequest()
                )

                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

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




}