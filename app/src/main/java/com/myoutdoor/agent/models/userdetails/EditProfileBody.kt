package com.myoutdoor.agent.models.userdetails

data class EditProfileBody(
    var authenticationType: String,
    var city: String,
    var clubName: Any,
    var email: String,
    var firstName: String,
    var getNotifications: Boolean,
    var groupName: String,
    var isUserProfileComplete: Boolean,
    var lastName: String,
    var phone: String,
    var st: String,
    var stateName: Any,
    var status: Int,
    var streetAddress: String,
    var userAccountID: Int,
    var userProfileID: Int,
    var zip: String
)