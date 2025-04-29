package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("caption")
    var caption: String,
    @SerializedName("imageFileName")
    var imageFileName: String,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productImageID")
    var productImageID: Int
)