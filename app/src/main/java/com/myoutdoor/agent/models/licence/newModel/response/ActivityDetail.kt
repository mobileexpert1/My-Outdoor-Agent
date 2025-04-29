package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class ActivityDetail(
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
    var address: String,
    @SerializedName("agreementName")
    var agreementName: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("clientLogoPath")
    var clientLogoPath: Any,
    @SerializedName("countyID")
    var countyID: Int,
    @SerializedName("countyName")
    var countyName: String,
    @SerializedName("currentDateTime")
    var currentDateTime: String,
    @SerializedName("displayDescription")
    var displayDescription: String,
    @SerializedName("displayName")
    var displayName: String,
    @SerializedName("guestPassAllowedDays")
    var guestPassAllowedDays: Int,
    @SerializedName("guestPassCost")
    var guestPassCost: Double,
    @SerializedName("hostApprovalRequired")
    var hostApprovalRequired: Boolean,
    @SerializedName("landownerEmail")
    var landownerEmail: Any,
    @SerializedName("landownerName")
    var landownerName: Any,
    @SerializedName("licenseActivityID")
    var licenseActivityID: Int,
    @SerializedName("licenseAgreementURL")
    var licenseAgreementURL: String,
    @SerializedName("licenseEndDate")
    var licenseEndDate: String,
    @SerializedName("licenseFee")
    var licenseFee: Double,
    @SerializedName("licenseStartDate")
    var licenseStartDate: String,
    @SerializedName("maxGuestsAllowed")
    var maxGuestsAllowed: Int,
    @SerializedName("maxMembersAllowed")
    var maxMembersAllowed: Int,
    @SerializedName("maxSaleQtyAllowed")
    var maxSaleQtyAllowed: Int,
    @SerializedName("memberPassCost")
    var memberPassCost: Double,
    @SerializedName("paymentDueDate")
    var paymentDueDate: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("preSale")
    var preSale: Int,
    @SerializedName("preSaleCharge")
    var preSaleCharge: Double,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productNo")
    var productNo: String,
    @SerializedName("productType")
    var productType: String,
    @SerializedName("productTypeID")
    var productTypeID: Int,
    @SerializedName("propertyName")
    var propertyName: String,
    @SerializedName("publicKey")
    var publicKey: String,
    @SerializedName("regionName")
    var regionName: String,
    @SerializedName("saleCount")
    var saleCount: Int,
    @SerializedName("saleStartDateTime")
    var saleStartDateTime: String,
    @SerializedName("state")
    var state: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("timeZone")
    var timeZone: String
)