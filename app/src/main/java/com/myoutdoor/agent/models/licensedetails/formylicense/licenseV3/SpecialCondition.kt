package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class SpecialCondition(
    @SerializedName("specCndDesc")
    var specCndDesc: String,
    @SerializedName("specCndID")
    var specCndID: Int,
    @SerializedName("specCndRefID")
    var specCndRefID: Int
)