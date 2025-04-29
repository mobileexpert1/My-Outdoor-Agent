package com.myoutdoor.agent.models.mylicences.pendinglicences

data class PendingInvitesLicencesResponse(
    var message: String,
    var model: List<PendingModel>,
    var statusCode: Int
)