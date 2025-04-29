package com.myoutdoor.agent.models.verification

data class SendVerificationCodeResponse(
    var message: String,
    var model: Any,
    var statusCode: Int
)

