package com.myoutdoor.agent.retrofit

import com.myoutdoor.agent.fragment.licence.model.LicenceKeyBody
import com.myoutdoor.agent.fragment.licence.model.LicenceResponseData
import com.myoutdoor.agent.fragment.message_new.model.get_messages.GetAllMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.get_messages.GetAllMessagesResponse
import com.myoutdoor.agent.fragment.message_new.model.refresh_messages.RefreshMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.refresh_messages.RefreshMessagesResponse
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageRequest
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageResponse
import com.myoutdoor.agent.fragment.verification.code.body.SendVerificationBody
import com.myoutdoor.agent.fragment.verification.code.body.UpdatePasswordBody
import com.myoutdoor.agent.fragment.verification.code.body.VerifyCodeBody
import com.myoutdoor.agent.fragment.verification.code.response.ForgotPasswordResponse
import com.myoutdoor.agent.fragment.verification.code.response.UpdatePasswordResponse
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassRequest
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassResponse
import com.myoutdoor.agent.models.EmptyData
import com.myoutdoor.agent.models.ForgotPassword.ForgotPasswordBody
import com.myoutdoor.agent.models.RluMapDetails.RLUMapResponse
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.accept_license.requestbody.AcceptLicenseBody
import com.myoutdoor.agent.models.accept_license.response.AcceptLicenseResponse
import com.myoutdoor.agent.models.addclubmember.AddClubMemberBody
import com.myoutdoor.agent.models.addclubmember.AddClubMemberResponse
import com.myoutdoor.agent.models.addupdatevehicle.AddUpdateVehicleBody
import com.myoutdoor.agent.models.addupdatevehicle.AddUpdateVehicleResponse
import com.myoutdoor.agent.models.changepassword.ChangePasswordBody
import com.myoutdoor.agent.models.changepassword.ChangePasswordResponse
import com.myoutdoor.agent.models.clubmemberdetails.ClubMemberDetailsBody
import com.myoutdoor.agent.models.clubmemberdetails.ClubMemberDetailsResponse
import com.myoutdoor.agent.models.contactus.ContactUsBody
import com.myoutdoor.agent.models.contactus.ContactUsResponse
import com.myoutdoor.agent.models.deletesearch.DeleteSearchBody
import com.myoutdoor.agent.models.deletesearch.DeleteSearchResponse
import com.myoutdoor.agent.models.fill_map_areas.FillMapAreasResponse
import com.myoutdoor.agent.models.freeaccess.FreeAccessBody
import com.myoutdoor.agent.models.freeaccess.response.FreeAccessResponse
import com.myoutdoor.agent.models.gate_access_points.GateAccessPointsResponse
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenResponse
import com.myoutdoor.agent.models.getallamenities.GetAllAmenitiesResponse
import com.myoutdoor.agent.models.getallstates.GetAllStatesResponse
import com.myoutdoor.agent.models.getavailablecountiesbystate.GetAvailableCountiesByStateBody
import com.myoutdoor.agent.models.getavailablecountiesbystate.GetAvailableCountiesByStateResponse
import com.myoutdoor.agent.models.getavailablestates.GetAvailableStatesResponse
import com.myoutdoor.agent.models.home.HomeResponse
import com.myoutdoor.agent.models.invitemember.InviteMemberBody
import com.myoutdoor.agent.models.invitemember.InviteMemberResponse
import com.myoutdoor.agent.models.licence.newModel.response.ActivityDetailV3Response
import com.myoutdoor.agent.models.licensedetails.forhome.LicenceViewDetailHomeBody
import com.myoutdoor.agent.models.licensedetails.forhome.LicenseNowViewDetailResponse
import com.myoutdoor.agent.models.licensedetails.formylicense.license.MyDetailsV2Body
import com.myoutdoor.agent.models.licensedetails.formylicense.license.MyDetailsV2Response
import com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.LicenceDetailV3Response
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingResponse
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractRequest
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.generate_contract.GenerateContractResponse
import com.myoutdoor.agent.models.listyourproperty.ListYourPropertyBody
import com.myoutdoor.agent.models.listyourproperty.ListYourPropertyResponse
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsBody
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsResponse
import com.myoutdoor.agent.models.memberRemove.MemberRemoveBody
import com.myoutdoor.agent.models.memberRemove.MemberRemoveResponse
import com.myoutdoor.agent.models.message.MessageResponse
import com.myoutdoor.agent.models.multi_polygons.MultipolyegonResponse
import com.myoutdoor.agent.models.mylicences.activelicences.ActiveLicencesResponse
import com.myoutdoor.agent.models.mylicences.expiredlicences.ExpiredLicencesResponse
import com.myoutdoor.agent.models.mylicences.memberoflicences.MemberOfLicencesResponse
import com.myoutdoor.agent.models.mylicences.pendinglicences.PendingInvitesLicencesResponse
import com.myoutdoor.agent.models.point_layer.PointLayerResponse
import com.myoutdoor.agent.models.preapprovalrequest.PreRequestResponse
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestResponse
import com.myoutdoor.agent.models.preapprovalrequest.request.PreApprovalRequest
import com.myoutdoor.agent.models.preapprovalrequest.request.PreApprovalRequestResponse
import com.myoutdoor.agent.models.product_type_rlu.CARluResponse
import com.myoutdoor.agent.models.propertylicense.PropertyLicenseBody
import com.myoutdoor.agent.models.propertylicense.PropertyLicenseResponse
import com.myoutdoor.agent.models.register.LoginBody
import com.myoutdoor.agent.models.register.LoginResponse
import com.myoutdoor.agent.models.register.RegisterBody
import com.myoutdoor.agent.models.register.RegisterResponse
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryResponse
import com.myoutdoor.agent.models.savedsearches.getsavedsearches.GetSavedSearchesResponse
import com.myoutdoor.agent.models.savedsearches.postsavedsearches.PostSaveSearchesBody
import com.myoutdoor.agent.models.savedsearches.postsavedsearches.PostSaveSearchesResponse
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillBody
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillResponse
import com.myoutdoor.agent.models.search.SearchBody
import com.myoutdoor.agent.models.search.SearchResponse
import com.myoutdoor.agent.models.search.searchV2.SearchV2Response
import com.myoutdoor.agent.models.search.searchV2.body.SearchV2Body
import com.myoutdoor.agent.models.simple_polygen_layer.SimplePolygenResponse
import com.myoutdoor.agent.models.sociallogin.SocailLoginBody
import com.myoutdoor.agent.models.sociallogin.SocailLoginResponse
import com.myoutdoor.agent.models.userdetails.EditProfileBody
import com.myoutdoor.agent.models.userdetails.EditProfileResponse
import com.myoutdoor.agent.models.userdetails.deleteruser.DeleteUserResponse
import com.myoutdoor.agent.models.userdetails.getuserdetails.GetUserDetailsResponse
import com.myoutdoor.agent.models.vehicleremove.VehicleRemoveBody
import com.myoutdoor.agent.models.vehicleremove.VehicleRemoveResponse
import com.myoutdoor.agent.models.verification.SendVerificationCodeResponse
import com.myoutdoor.agent.utils.BaseData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {


    @POST("account/userregister")
    suspend fun registerRequest(
        @Body registerBody: RegisterBody
    ): BaseData<RegisterResponse>

    @POST("account/userregister")
    suspend fun signupRequest(
        @Body signupBody: SocailLoginBody
    ): SocailLoginResponse

    @POST("account/Authorize")
    suspend fun loginRequest(
        @Body loginBody: LoginBody
    ): BaseData<LoginResponse>

    @POST("account/forgotpasswordv2")
    suspend fun forgotRequest(
        @Body forgotPasswordBody: ForgotPasswordBody
    ): ForgotPasswordResponse

    /* @POST("account/forgotpasswordv2")
        @Body forgotPasswordBody: ForgotPasswordBody
    ): BaseData<ForgotPasswordResponse>*/

    @POST("account/sendverificationcode")
    suspend fun sendverificationcodeRequest(
        @Body sendVerificationBody: SendVerificationBody
    ): SendVerificationCodeResponse

    @POST("account/updatepasswordv2")
    suspend fun updatepasswordRequest(
        @Body updatePasswordBody: UpdatePasswordBody
    ): UpdatePasswordResponse

    @POST("account/verifyCode")
    suspend fun verifyCodeRequest(
        @Body verifyCodeBody: VerifyCodeBody
    ): SendVerificationCodeResponse

    /* @POST("account/forgotpassword")
    suspend fun forgotRequest(
        @Body forgotPasswordBody: ForgotPasswordBody
    ): BaseData<EmptyData>*/

    //    @GET("search/mypreapprovalrequests")
//    suspend fun preApprovalRequest(
//    ): PreRequestResponse
    @GET("search/mypreapprovalrequestsv2")
    suspend fun preApprovalRequest(
    ): PreRequestResponse


    @GET("search/getavailablestates")
    suspend fun getAvailableStatesRequest(
    ): GetAvailableStatesResponse

    @GET("search/getallamenities")
    suspend fun getAllAmenitiesRequest(
    ): GetAllAmenitiesResponse

    @GET("message/myconversations")
    suspend fun messageRequest(
    ): MessageResponse

    @GET("search/getallstates")
    suspend fun getAllStatesRequest(
    ): GetAllStatesResponse

    @GET("search/SavedSearches")
    suspend fun getSavedSearchesRequest(
    ): GetSavedSearchesResponse

    @GET("license/memberoflicenses")
    suspend fun memberOfLicencesRequest(
    ): MemberOfLicencesResponse

    @GET("license/mylicenses")
    suspend fun mylicenses(
    ): ActiveLicencesResponse

    @GET("license/pendinginviteslicenses")
    suspend fun pendinginviteslicensesRequest(
    ): PendingInvitesLicencesResponse

    @GET("license/expiredlicenses")
    suspend fun expiredLicencesRequest(
    ): ExpiredLicencesResponse

    @POST("search/cancelpreapprovalrequest")
    suspend fun preApprovalCancelRequest(
        @Body preApprovalCancelRequestBody: PreApprovalCancelRequestBody
    ): PreApprovalCancelRequestResponse

    @POST("user/userprofile")
    suspend fun userDetailsRequest(
    ): GetUserDetailsResponse

    @POST("Account/ContactUs")
    suspend fun contactUsRequest(
        @Body contactUsBody: ContactUsBody
    ): ContactUsResponse

    @POST("search/SaveSearch")
    suspend fun postSaveSearchesRequest(
        @Body postSaveSearchesBody: PostSaveSearchesBody
    ): PostSaveSearchesResponse

    @POST("search/searchAutofill")
    suspend fun searchAutoFillRequest(
        @Body autoFillBody: SearchAutoFillBody
    ): SearchAutoFillResponse


    @POST("search/SearchV2")
    suspend fun searchRequest(
        @Body searchBody: SearchV2Body
    ): SearchV2Response

//    @POST("search/search")
//        suspend fun searchRequest(
//            @Body searchBody: SearchBody
//        ): SearchResponse

    @POST("Search/getavailablecountiesbystate")
    suspend fun getAvailableCountiesByStateRequest(
        @Body getAvailableCountiesByStateBody: GetAvailableCountiesByStateBody
    ): GetAvailableCountiesByStateResponse

    @POST("search/deletesearch")
    suspend fun deletesearchRequest(
        @Body deleteSearchBody: DeleteSearchBody
    ): DeleteSearchResponse


    //    https://datav2.myoutdooragent.com/api/account/DeleteUser
    @POST("account/DeleteUser")
    suspend fun deleteUserRequest(
    ): DeleteUserResponse

    @POST("Property/ActivityDetailV2")
    suspend fun licenseViewDetailsRequest(
        @Body licenseViewDetailsBody: LicenceViewDetailHomeBody
    ): LicenseNowViewDetailResponse

    @POST("License/rightofentryform")
    suspend fun rightOfEntryRequest(
        @Body rightOfEntryBody: RightOfEntryBody
    ): RightOfEntryResponse

    /*    @POST("license/LicenseDetailV3")
            suspend fun myLicenseDetailV2Request(
                @Body myLicenseDetailV2Body: MyDetailsV2Body
            ): LicenceDetailV3Response*/
    @POST("license/LicenseDetailv3")
    suspend fun myLicenseDetailV2Request(
        @Body myLicenseDetailV2Body: MyDetailsV2Body
    ): MyDetailsV2Response

    /*        @POST("license/LicenseDetailV2")
            suspend fun myLicenseDetailV2Request(
                @Body myLicenseDetailV2Body: MyDetailsV2Body
            ): MyDetailsV2Response*/

    @POST("license/addUpdateVehicle")
    suspend fun addUpdateVehicleRequest(
        @Body addUpdateVehicleBody: AddUpdateVehicleBody
    ): AddUpdateVehicleResponse

//    https://datav2.myoutdooragent.com/api/license/acceptlicenserequest

    @POST("license/acceptlicenserequest")
    suspend fun acceptlicenseRequest(
        @Body acceptLicenseBody: AcceptLicenseBody
    ): AcceptLicenseResponse


    @POST("club/generateclubmembershipcard")
    suspend fun clubMemberDetailsRequest(
        @Body clubMemberDetailsBody: ClubMemberDetailsBody
    ): ClubMemberDetailsResponse

    @POST("License/generateContractv2")
    suspend fun propertyLicenseRequest(
        @Body propertyLicenseBody: PropertyLicenseBody
    ): PropertyLicenseResponse

    @POST("license/deleteVehicle")
    suspend fun vehicleRemoveRequest(
        @Body vehicleRemoveBody: VehicleRemoveBody
    ): VehicleRemoveResponse

    @POST("license/invitemember")
    suspend fun inviteMemberRequest(
        @Body inviteMemberBody: InviteMemberBody
    ): InviteMemberResponse

    @POST("user/changepassword")
    suspend fun changePasswordRequest(
        @Body changePasswordBody: ChangePasswordBody
    ): ChangePasswordResponse

    @POST("account/listyourproperty")
    suspend fun listYourPropertyRequest(
        @Body listYourPropertyBody: ListYourPropertyBody
    ): ListYourPropertyResponse

    @POST("user/editprofile")
    suspend fun editProfileRequest(
        @Body editProfileBody: EditProfileBody
    ): EditProfileResponse

    @POST("account/manageNotifications")
    suspend fun manageNotificationRequest(
        @Body manageNotificationsBody: ManageNotificationsBody
    ): ManageNotificationsResponse

    @GET("property/regionwiseproperties")
    suspend fun regionwiseproperties(
    ): HomeResponse

    /*    @POST("Property/ActivityDetailV2")
      suspend fun licenceRequest(
          @Body licenceKeyBody: LicenceKeyBody
      ): LicenceResponseData  */

    @POST("Property/ActivityDetailV3")
    suspend fun licenceRequest(
        @Body licenceKeyBody: LicenceKeyBody
    ): ActivityDetailV3Response

    @POST("permit/freeaccess")
    suspend fun freeAccessRequest(
        @Body freeAccessBody: FreeAccessBody
    ): FreeAccessResponse

    @POST("license/removemember")
    suspend fun removeMemberRequest(
        @Body memberRemoveBody: MemberRemoveBody
    ): MemberRemoveResponse


    @POST("club/addClubMember")
    suspend fun addClubRequest(
        @Body addClubMemberBody: AddClubMemberBody
    ): AddClubMemberResponse


    @POST("payment/getpaymenttoken")
    suspend fun getPaymentToken(
        @Body getPaymentTokenBody: GetPaymentTokenBody
    ): GetPaymentTokenResponse


    @POST("Property/daypassavailibility")
    suspend fun dayPassAvailibilityRequest(
        @Body model: DayPassRequest
    ): DayPassResponse


    @POST("Message/GetAllMessages")
    suspend fun getAllMessages(
        @Body model: GetAllMessagesRequest
    ): GetAllMessagesResponse

    @POST("Message/sendmessage")
    @Multipart
    suspend fun sendMessage(
        @Part("MessageText") MessageText: RequestBody,
        @Part("ProductID") ProductID: RequestBody
    ): SendMessageResponse

    /*    @POST("Message/sendmessage")
    suspend fun sendMessage(
        @Body model: SendMessageRequest
    ): SendMessageResponse*/

    @POST("Message/RefreshMessages")
    suspend fun refreshMessages(
        @Body model: RefreshMessagesRequest
    ): RefreshMessagesResponse

    @POST("search/preapprovalrequestv2")
    suspend fun preapprovalrequestv2(
        @Body model: PreApprovalRequest
    ): PreApprovalRequestResponse

    @POST("license/addharvestingdetails")
    suspend fun addHarvestingRequest(
        @Body addHarvestingRequest: AddHarvestingRequest
    ): AddHarvestingResponse

    @POST("License/generateContractv2")
    suspend fun generateContractv2Request(
        @Body generateContractRequest: GenerateContractRequest
    ): GenerateContractResponse

//    https://maps.myoutdooragent.com/arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/4/query?where=1%3D1&outFields=*&returnGeometry=true&f=geojson

    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/2/query")
    suspend fun particularPolygonLayer(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("spatialRel") spatialRel: String,
        @Query("f") f: String
    ): ResponseBody

    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/4/query")
    suspend fun particularPolygonFourLayer(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("spatialRel") spatialRel: String,
        @Query("f") f: String
    ): SimplePolygenResponse

    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/2/query")
    suspend fun simplePolygonLayer(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("spatialRel") spatialRel: String,
        @Query("f") f: String
    ): SimplePolygenResponse

//    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/4/query")
    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/2/query")
    suspend fun multiPolygonLayer(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("returnGeometry") returnGeometry: Boolean,
        @Query("f") f: String
    ): ResponseBody

 @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/4/query")
    suspend fun multiPolygonLayerFour(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("returnGeometry") returnGeometry: Boolean,
        @Query("f") f: String
    ): ResponseBody

//    /arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/3/query?where=ProductType='Non-Motorized'&outFields=*&returnGeometry=true&f=geojson

    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/3/query")
    suspend fun fillAreas(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("returnGeometry") returnGeometry: Boolean,
        @Query("f") f: String
    ): FillMapAreasResponse


    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/0/query")
    suspend fun gateAccessPoints(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("returnGeometry") returnGeometry: Boolean,
        @Query("f") f: String
    ): GateAccessPointsResponse

//    /arcgisweb/rest/services/RLMS/LicenseMapBackground/MapServer/36/query?f=geojson&outFields=STATE_ABBR&spatialRel=esriSpatialRelIntersects&where=STATE_ABBR%3D%27AR%27
//    STATE_ABBR='AR'
    @GET("/arcgisweb/rest/services/RLMS/LicenseMapBackground/MapServer/36/query")
    suspend fun selectedStates(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("spatialRel") spatialRel: String,
        @Query("f") f: String
    ): SimplePolygenResponse
//        CARluResponse


    @POST("property/rlumapdetail")
    suspend fun RluMapDetail(
        @Body rluBody: RluBody
    ): RLUMapResponse


    // 1st
    @GET("arcgisweb/rest/services/MOA/MOA_Map_Live/MapServer/1/query")
    suspend fun pointLayer(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("returnGeometry") returnGeometry: Boolean,
        @Query("f") f: String
    ): PointLayerResponse


}
