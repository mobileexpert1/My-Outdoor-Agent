package com.myoutdoor.agent.models.search.searchV2


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Model(
    @SerializedName("acres")
    var acres: Double,
    @SerializedName("activityDescription")
    var activityDescription: Any,
    @SerializedName("activityEndDate")
    var activityEndDate: String,
    @SerializedName("activityNumber")
    var activityNumber: String,
    @SerializedName("activityStartDate")
    var activityStartDate: String,
    @SerializedName("activityType")
    var activityType: String,
    @SerializedName("address")
    var address: Any,
    @SerializedName("agreementName")
    var agreementName: Any,
    @SerializedName("amenities")
    var amenities: String,
    @SerializedName("amenitiyList")
    var amenitiyList: List<Amenitiy>,
    @SerializedName("city")
    var city: Any,
    @SerializedName("clientID")
    var clientID: Int,
    @SerializedName("clientLogoPath")
    var clientLogoPath: String,
    @SerializedName("clientName")
    var clientName: String,
    @SerializedName("clientPropertyID")
    var clientPropertyID: Int,
    @SerializedName("clientPublicKey")
    var clientPublicKey: String,
    @SerializedName("clientRegionID")
    var clientRegionID: Int,
    @SerializedName("countyID")
    var countyID: Int,
    @SerializedName("countyName")
    var countyName: String,
    @SerializedName("currentDateTime")
    var currentDateTime: String,
    @SerializedName("displayDescription")
    var displayDescription: Any,
    @SerializedName("displayName")
    var displayName: String,
    @SerializedName("guestPassAllowedDays")
    var guestPassAllowedDays: Int,
    @SerializedName("guestPassCost")
    var guestPassCost: Double,
    @SerializedName("hostApprovalRequired")
    var hostApprovalRequired: Boolean,
    @SerializedName("imagefilename")
    var imagefilename: String,
    @SerializedName("isPurchased")
    var isPurchased: Any,
    @SerializedName("licenseActivityID")
    var licenseActivityID: Int,
    @SerializedName("licenseAgreementURL")
    var licenseAgreementURL: Any,
    @SerializedName("licenseEndDate")
    var licenseEndDate: String,
    @SerializedName("licenseFee")
    var licenseFee: Double,
    @SerializedName("licenseStartDate")
    var licenseStartDate: String,
    @SerializedName("licenseType")
    var licenseType: Any,
    @SerializedName("maxGuestsAllowed")
    var maxGuestsAllowed: Int,
    @SerializedName("maxMembersAllowed")
    var maxMembersAllowed: Int,
    @SerializedName("maxSaleQtyAllowed")
    var maxSaleQtyAllowed: Int,
    @SerializedName("memberPassCost")
    var memberPassCost: Double,
    @SerializedName("motorizedAccess")
    var motorizedAccess: Boolean,
    @SerializedName("phone")
    var phone: Any,
    @SerializedName("preApprRequestID")
    var preApprRequestID: Int,
    @SerializedName("preSale")
    var preSale: Int,
    @SerializedName("preSaleCharge")
    var preSaleCharge: Double,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productNo")
    var productNo: String,
    @SerializedName("productType")
    var productType: Any,
    @SerializedName("productTypeID")
    var productTypeID: Int,
    @SerializedName("propertyName")
    var propertyName: Any,
    @SerializedName("propertyUserStatus")
    var propertyUserStatus: Int,
    @SerializedName("publicKey")
    var publicKey: String,
    @SerializedName("regionName")
    var regionName: Any,
    @SerializedName("renewalStatus")
    var renewalStatus: Int,
    @SerializedName("requestStatus")
    var requestStatus: Any,
    @SerializedName("saleCount")
    var saleCount: Int,
    @SerializedName("saleStartDateTime")
    var saleStartDateTime: String,
    @SerializedName("specCndDesc")
    var specCndDesc: Any,
    @SerializedName("state")
    var state: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("timeZone")
    var timeZone: String
) : java.io.Serializable