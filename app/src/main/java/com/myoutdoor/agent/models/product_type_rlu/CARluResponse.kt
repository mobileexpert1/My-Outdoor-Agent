package com.myoutdoor.agent.models.product_type_rlu


import com.google.gson.annotations.SerializedName

data class CARluResponse(
    @SerializedName("features")
    var features: List<Feature>,
    @SerializedName("type")
    var type: String
)