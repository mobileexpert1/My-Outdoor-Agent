package com.myoutdoor.agent.models.preapprovalrequest.request

data class PreApprovalRequest(
    val LicenseActivityID: String,
    val ProductID: String,
    val RequestComments: String,
    val UserAccountID: String
)