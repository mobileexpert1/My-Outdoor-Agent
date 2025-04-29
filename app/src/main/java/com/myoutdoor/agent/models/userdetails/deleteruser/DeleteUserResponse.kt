package com.myoutdoor.agent.models.userdetails.deleteruser

data class DeleteUserResponse(
    var message: String,
    var model: Boolean,
    var statusCode: Int
)