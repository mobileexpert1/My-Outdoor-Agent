package com.myoutdoor.agent.models.sociallogin

data class Model(
    var accountType: String = "",
    var authToken: String = "",
    var authenticationType: String = "",
    var city: String = "",
    var dateCreated: String = "",
    var dateLastLogin: String = "",
    var description: String = "",
    var email: String = "",
    var firstName: String = "",
    var getNotifications: Boolean?,
    var groupName: String = "",
    var isBlacklisted: Boolean?,
    var isEmailVerified: Boolean?,
    var isUserProfileComplete: Boolean?,
    var lastName: String= "",
    var message: String = "",
    var name: String = "",
    var phone: String = "",
    var st: String = "",
    var status: String = "",
    var streetAddress: String = "",
    var token: String = "",
    var userAccountID: String = "",
    var userProfileID: String = "",
    var zip: String = ""
)