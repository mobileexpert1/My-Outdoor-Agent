package com.myoutdoor.agent.fragment.message_new.model.send_messages

data class SendMessageResponse(
    val message: String,
    val model: String,
    val statusCode: Int
)