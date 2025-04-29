package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class RenewalActivity(
    @SerializedName("licenseActivityID")
    var licenseActivityID: Int,
    @SerializedName("paymentDueDate")
    var paymentDueDate: String,
    @SerializedName("publicKey")
    var publicKey: Any
)