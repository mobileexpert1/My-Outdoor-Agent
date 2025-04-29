package com.myoutdoor.agent.models.DayPassAvailibility

data class DayPassRequest(
    var licenseActivityID: String,
    var dateOfArrival: String,
    var daysCount: String
)