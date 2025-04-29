package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class ClientDetails(
    @SerializedName("addClubMembers")
    var addClubMembers: Boolean,
    @SerializedName("allowTraxAppAccess")
    var allowTraxAppAccess: Boolean,
    @SerializedName("clientAdministration")
    var clientAdministration: Boolean,
    @SerializedName("clientCurrentFY")
    var clientCurrentFY: Boolean,
    @SerializedName("clientID")
    var clientID: Int,
    @SerializedName("clientSite")
    var clientSite: String,
    @SerializedName("clubMemberLicense")
    var clubMemberLicense: Boolean,
    @SerializedName("deerHarvestInfo")
    var deerHarvestInfo: Boolean,
    @SerializedName("externalLogins")
    var externalLogins: Boolean,
    @SerializedName("recManualChecks")
    var recManualChecks: Boolean,
    @SerializedName("rightofEntry")
    var rightofEntry: Boolean,
    @SerializedName("roEAgreement")
    var roEAgreement: Boolean
)