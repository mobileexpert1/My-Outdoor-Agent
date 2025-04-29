package com.myoutdoor.agent.models.accept_license.requestbody


import com.google.gson.annotations.SerializedName

data class AcceptLicenseBody(
    @SerializedName("LicenseContractID")
    var licenseContractID: Int,
    @SerializedName("UserAccountID")
    var userAccountID: Int
)