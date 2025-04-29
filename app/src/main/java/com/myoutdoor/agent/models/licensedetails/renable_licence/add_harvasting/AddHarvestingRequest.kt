package com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting

data class AddHarvestingRequest(
    val bearCount: String,
    val buckCount: String,
    val doeCount: String,
    val huntingSeason: Int,
    val productID: Int,
    val turkeyCount: String
)