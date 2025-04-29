package com.myoutdoor.agent.models.register

data class LoginBody(
    var Email:String,
    var Password:String,
    var AuthenticationType:String,
    var AuthorizationKey:String,
)
