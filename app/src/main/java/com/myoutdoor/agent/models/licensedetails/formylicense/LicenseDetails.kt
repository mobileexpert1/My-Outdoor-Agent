package com.myoutdoor.agent.models.licensedetails.formylicense

data class LicenseDetails(
    var acres: Double,
    var active: Any,
    var activityNumber: String,
    var activityType: String,
    var agreementName: String,
    var allowMemberActions: Boolean,
    var amount: Int,
    var contactNumber: String,
    var contractStatus: String,
    var countyName: String,
    var displayDescription: String,
    var displayName: Any,
    var email: String,
    var firstName: String,
    var fundName: String,
    var groupName: String,
    var guestPassAllowedDays: Int,
    var guestPassCost: Double,
    var imageFilename: String,
    var invoiceType: Any,
    var isAccepted: Int,
    var laPublicKey: Any,
    var lastName: String,
    var lcPublicKey: Any,
    var licenseActivityID: Int,
    var licenseAgreement: Any,
    var licenseContractID: Int,
    var licenseEndDate: String,
    var licenseFee: Double,
    var licenseStartDate: String,
    var licenseStatus: String,
    var maxGuestsAllowed: Int,
    var maxMembersAllowed: Int,
    var memberPassCost: Double,
    var motorizedAccess: Boolean,
    var paymentDueDate: String,
    var paymentStatus: String,
    var paymentType: String,
    var phone: String,
    var productID: Int,
    var productNo: String,
    var productTypeID: Int,
    var propertyName: String,
    var publicKey: Any,
    var renewalStatus: Int,
    var stateName: String,
    var status: Any
)