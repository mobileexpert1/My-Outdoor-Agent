package com.myoutdoor.agent.models.register

data class RegisterResponse(
    val firstName:String,
    val lastName:String,
    val streetAddress:String,
    val city:String,
    val st:String,
    val zip:String,
    val phone:String,
    val groupName:String,
    val getNotifications:Boolean,
    val isBlacklisted:Boolean,
    val isEmailVerified:Boolean,
    val email:String,
    val password:String,
    val accountType:String,
    val authenticationKey:String,
    val authorizationKey:String,
    val authenticationType:String,
    val clientToken:String,
    val sourceClientID:String,

)
