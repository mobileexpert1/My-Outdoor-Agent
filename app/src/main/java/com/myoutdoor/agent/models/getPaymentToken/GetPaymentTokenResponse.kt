package com.myoutdoor.agent.models.getPaymentToken

data class GetPaymentTokenResponse(
    var message: String,
    var model: Model,
    var statusCode: Int
)