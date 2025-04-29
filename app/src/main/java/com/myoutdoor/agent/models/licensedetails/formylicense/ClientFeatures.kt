package com.myoutdoor.agent.models.licensedetails.formylicense

data class ClientFeatures(
    var addClubMembers: Boolean,
    var allowTraxAppAccess: Boolean,
    var clientAdministration: Boolean,
    var clientCurrentFY: Boolean,
    var clientID: Int,
    var clientSite: String,
    var clubMemberLicense: Boolean,
    var deerHarvestInfo: Boolean,
    var externalLogins: Boolean,
    var recManualChecks: Boolean,
    var rightofEntry: Boolean,
    var roEAgreement: Boolean
)