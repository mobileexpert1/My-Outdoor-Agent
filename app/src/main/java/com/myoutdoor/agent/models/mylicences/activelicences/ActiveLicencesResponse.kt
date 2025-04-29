package com.myoutdoor.agent.models.mylicences.activelicences

data class ActiveLicencesResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)