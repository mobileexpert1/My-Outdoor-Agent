package com.myoutdoor.agent.models.home

data class HomeResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)