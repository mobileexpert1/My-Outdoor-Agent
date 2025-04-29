package com.myoutdoor.agent.models.mylicences.memberoflicences

data class MemberOfLicencesResponse(
    val statusCode: Int,
    val message: String ="",
    val model: List<MemberModel>
)