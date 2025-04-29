package com.myoutdoor.agent.models.getallstates

data class GetAllStatesResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)