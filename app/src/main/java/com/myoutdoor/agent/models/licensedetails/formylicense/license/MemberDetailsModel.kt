package com.myoutdoor.agent.models.licensedetails.formylicense.license

data class MemberDetailsModel(
    var feePerMember: Double,
    var maxMembersAllowed: Int,
    var memberFeesAmountToPay: Double,
    var showPayMemberFeesButton: Boolean,
    var totalMembersAdded: Int,
    var unpaidMembersCount: Int
)