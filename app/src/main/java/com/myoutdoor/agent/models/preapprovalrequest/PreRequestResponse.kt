package com.myoutdoor.agent.models.preapprovalrequest

data class PreRequestResponse(
    var message: String,
    var model: List<Data>,
    var statusCode: Int
)