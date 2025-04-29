package com.myoutdoor.agent.models.register

data class LoginResponse(
    var userProfileID:String,
    var userAccountID:String,
    var firstName:String,
    var lastName:String,
    var streetAddress:String,
    var city:String,
    var st:String,
    var zip:String,
    var phone:String,
    var groupName:String,
    var isBlacklisted:Boolean,
    var isEmailVerified:Boolean,
    var email:String,
    var getNotifications:Boolean,
    var authenticationType:String,
    var message:String,
    var token:String,
    var isUserProfileComplete:String,
    var authToken:String,
    var dateCreated:String,
    var dateLastLogin:String,
    var accountType:String,
    var description:String,
    var name:String,
    var status:String


)
