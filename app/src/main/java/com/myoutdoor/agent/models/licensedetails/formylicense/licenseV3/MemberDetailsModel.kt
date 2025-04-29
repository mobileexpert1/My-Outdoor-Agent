package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class MemberDetailsModel(
    @SerializedName("feePerMember")
    var feePerMember: Double,
    @SerializedName("maxMembersAllowed")
    var maxMembersAllowed: Int,
    @SerializedName("memberFeesAmountToPay")
    var memberFeesAmountToPay: Double,
    @SerializedName("showPayMemberFeesButton")
    var showPayMemberFeesButton: Boolean,
    @SerializedName("totalMembersAdded")
    var totalMembersAdded: Int,
    @SerializedName("unpaidMembersCount")
    var unpaidMembersCount: Int
)