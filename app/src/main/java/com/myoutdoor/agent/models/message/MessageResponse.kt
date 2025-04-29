package com.myoutdoor.agent.models.message

data class MessageResponse(
    val message: String,
    val model: List<Model>,
    val statusCode: Int
)