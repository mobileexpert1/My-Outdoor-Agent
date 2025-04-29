package com.myoutdoor.agent.fragment.licence.model

data class ClientDetails(
    val addClubMembers: Boolean,
    val allowTraxAppAccess: Boolean,
    val clientAdministration: Boolean,
    val clientCurrentFY: Boolean,
    val clientID: Int,
    val clientSite: String,
    val clubMemberLicense: Boolean,
    val deerHarvestInfo: Boolean,
    val externalLogins: Boolean,
    val recManualChecks: Boolean,
    val rightofEntry: Boolean,
    val roEAgreement: Boolean
)