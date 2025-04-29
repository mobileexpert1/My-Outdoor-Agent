package com.myoutdoor.agent.models.managenotifications

data class ManageNotificationsResponse(
    var statusCode: Int,
    var message: String,
    var value: Any
)