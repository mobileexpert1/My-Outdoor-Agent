package com.myoutdoor.agent.models.product_type_rlu.arkansas


import com.google.gson.annotations.SerializedName

data class ArkansasRespnse(
    @SerializedName("features")
    var features: List<Feature>,
    @SerializedName("type")
    var type: String
)