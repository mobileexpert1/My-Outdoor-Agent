package com.myoutdoor.agent.models.gate_access_points


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("GateNo")
    var gateNo: String,
    @SerializedName("GateType")
    var gateType: String,
    @SerializedName("OBJECTID")
    var oBJECTID: Int,
    @SerializedName("RLUNo")
    var rLUNo: String
)