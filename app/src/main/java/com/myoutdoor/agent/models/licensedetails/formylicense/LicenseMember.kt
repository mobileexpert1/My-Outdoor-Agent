package com.myoutdoor.agent.models.licensedetails.formylicense

data class LicenseMember(
    var contractStatus: Any,
    var displayDescription: Any,
    var email: String,
    var firstName: String,
    var internalNotes: Any,
    var isAccepted: Boolean,
    var isPaid: Boolean,
    var lastName: String,
    var licenseContractID: Any,
    var licenseContractMemberID: String,
    var licenseStatus: Any,
    var licenseType: Any,
    var memberType: String,
    var userAccountID: Int,
    var userProfileID: Int
)