package com.myoutdoor.agent.fragment.message_new.model.get_messages

import java.util.ArrayList

data class GetAllMessagesResponse(
    val message: String,
    val model: ArrayList<Model>,
    val statusCode: Int
)