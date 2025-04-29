package com.myoutdoor.agent.models.preapprovalrequest.request

data class PreApprovalRequestResponse(
    val message: String,
    val model: Any,
    val statusCode: Int
)