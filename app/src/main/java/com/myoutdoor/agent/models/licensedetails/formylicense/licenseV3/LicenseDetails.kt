package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class LicenseDetails(
    @SerializedName("acres")
    var acres: Double,
    @SerializedName("active")
    var active: Any,
    @SerializedName("activityNumber")
    var activityNumber: String,
    @SerializedName("activityType")
    var activityType: String,
    @SerializedName("agreementName")
    var agreementName: String,
    @SerializedName("allowMemberActions")
    var allowMemberActions: Boolean,
    @SerializedName("amount")
    var amount: Int,
    @SerializedName("contactNumber")
    var contactNumber: String,
    @SerializedName("contractStatus")
    var contractStatus: String,
    @SerializedName("countyName")
    var countyName: String,
    @SerializedName("displayDescription")
    var displayDescription: String,
    @SerializedName("displayName")
    var displayName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("fundName")
    var fundName: String,
    @SerializedName("groupName")
    var groupName: String,
    @SerializedName("guestPassAllowedDays")
    var guestPassAllowedDays: Int,
    @SerializedName("guestPassCost")
    var guestPassCost: Double,
    @SerializedName("imageFilename")
    var imageFilename: String,
    @SerializedName("invoiceType")
    var invoiceType: Any,
    @SerializedName("isAccepted")
    var isAccepted: Int,
    @SerializedName("isPaid")
    var isPaid: Any,
    @SerializedName("laPublicKey")
    var laPublicKey: Any,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("lcPublicKey")
    var lcPublicKey: Any,
    @SerializedName("licenseActivityID")
    var licenseActivityID: Int,
    @SerializedName("licenseAgreement")
    var licenseAgreement: Any,
    @SerializedName("licenseContractID")
    var licenseContractID: Int,
    @SerializedName("licenseEndDate")
    var licenseEndDate: String,
    @SerializedName("licenseFee")
    var licenseFee: Double,
    @SerializedName("licenseStartDate")
    var licenseStartDate: String,
    @SerializedName("licenseStatus")
    var licenseStatus: String,
    @SerializedName("maxGuestsAllowed")
    var maxGuestsAllowed: Int,
    @SerializedName("maxMembersAllowed")
    var maxMembersAllowed: Int,
    @SerializedName("memberPassCost")
    var memberPassCost: Double,
    @SerializedName("memberType")
    var memberType: String,
    @SerializedName("motorizedAccess")
    var motorizedAccess: Boolean,
    @SerializedName("paymentDueDate")
    var paymentDueDate: String,
    @SerializedName("paymentStatus")
    var paymentStatus: String,
    @SerializedName("paymentType")
    var paymentType: String,
    @SerializedName("pendingLicenseFee")
    var pendingLicenseFee: Double,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productNo")
    var productNo: String,
    @SerializedName("productTypeID")
    var productTypeID: Int,
    @SerializedName("propertyName")
    var propertyName: String,
    @SerializedName("publicKey")
    var publicKey: Any,
    @SerializedName("renewalStatus")
    var renewalStatus: Int,
    @SerializedName("showAcceptButton")
    var showAcceptButton: Boolean,
    @SerializedName("showPayButton")
    var showPayButton: Boolean,
    @SerializedName("showPaymentButtons")
    var showPaymentButtons: Boolean,
    @SerializedName("stateName")
    var stateName: String,
    @SerializedName("status")
    var status: Any
)