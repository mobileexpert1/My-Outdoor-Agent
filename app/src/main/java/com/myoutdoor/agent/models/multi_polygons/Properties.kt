package com.myoutdoor.agent.models.multi_polygons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Properties {
    @SerializedName("RLUNo")
    @Expose
    var rLUNo: String? = null

    @SerializedName("IsLicensed")
    @Expose
    var isLicensed: Int? = null

    @SerializedName("ObjectId")
    @Expose
    var objectId: Int? = null
}