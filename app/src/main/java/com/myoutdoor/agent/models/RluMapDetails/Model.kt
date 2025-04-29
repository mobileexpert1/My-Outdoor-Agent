package com.myoutdoor.agent.models.RluMapDetails

data class Model(
    val acres: Double,
    val caption: Any,
    val countyName: String,
    val hostApprovalRequired: Boolean,
    val imageFilename: String,
    val isAvailable: Boolean,
    val licenseActivityID: Int,
    val licenseFee: Double,
    val productID: Int,
    val productNo: String,
    val propertyUserStatus: Int,
    val publicKey: String,
    val renewalStatus: Int,
    val requestStatus: Any,
    val stateName: String,
    val status: Any
)