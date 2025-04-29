package com.myoutdoor.agent.models.licensedetails.formylicense

data class ClubMemberDetail(
    var address: String,
    var city: String,
    var email: String,
    var firstName: String,
    var guestPassEndDate: String,
    var guestPassStartDate: String,
    var isPaid: Boolean,
    var lastName: String,
    var licenseContractID: Int,
    var licenseContractMemberID: Int,
    var memberType: String,
    var phone: Any,
    var state: String,
    var zip: String
)