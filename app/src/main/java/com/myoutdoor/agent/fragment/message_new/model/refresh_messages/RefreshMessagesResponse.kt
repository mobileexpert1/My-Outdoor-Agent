package com.myoutdoor.agent.fragment.message_new.model.refresh_messages

import com.myoutdoor.agent.fragment.message_new.model.get_messages.Model
import java.util.ArrayList

data class RefreshMessagesResponse(
    var message: String,
    var model: ArrayList<Model>,
    var statusCode: Int
)