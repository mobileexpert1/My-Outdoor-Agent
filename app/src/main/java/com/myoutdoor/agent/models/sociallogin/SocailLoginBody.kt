package com.myoutdoor.agent.models.sociallogin

data class SocailLoginBody(
    var AccountType: Int,
    var AuthenticationKey: String,
    var AuthenticationType: String,
    var AuthorizationKey: String,
    var City: String,
    var Email: String,
    var FirstName: String,
    var GetNotifications: Boolean,
    var GroupName: String,
    var IsBlacklisted: Boolean,
    var Password: String,
    var Phone: String,
    var SourceClientID: Int,
    var St: String,
    var StreetAddress: String,
    var ZIP: String
)