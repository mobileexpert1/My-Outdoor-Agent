package com.myoutdoor.agent.models.mylicences.memberoflicences

data class MemberModel(
    val acres: Double,
    val activityNumber: Any,
    val countyName: String,
    val displayName: String,
    val imageFilename: String,
    val licenseContractID: Int,
    val licenseEndDate: String,
    val licenseStartDate: String,
    val productNo: String,
    val productTypeID: Int,
    val publicKey: String,
    val stateName: String
)