package com.myoutdoor.agent.models.userdetails.getuserdetails

data class GetUserDetailsResponse(
    var message: String,
    var model: Model,
    var statusCode: Int
)