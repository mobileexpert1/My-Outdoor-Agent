package com.myoutdoor.agent.models.register

data class RegisterBody(
    var FirstName: String,
    var LastName: String,
    var Email: String,
    var Password: String,
    var ConfirmPassword: String,
    var AccountType: String,
    var AuthenticationKey: String,
    var AuthenticationType: String,
    var SourceClientID: String,
    )
