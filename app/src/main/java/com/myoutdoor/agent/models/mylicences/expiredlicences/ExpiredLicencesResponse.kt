package com.myoutdoor.agent.models.mylicences.expiredlicences

data class ExpiredLicencesResponse(
    var message: String,
    var model: List<ExpiredLicencesModel>,
    var statusCode: Int
)