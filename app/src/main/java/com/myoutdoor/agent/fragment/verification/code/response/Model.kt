package com.myoutdoor.agent.fragment.verification.code.response


import com.google.gson.annotations.SerializedName

data class Model(
//    @SerializedName("accountType")
//    var accountType: Int,
//    @SerializedName("authenticationKey")
//    var authenticationKey: Any,
//    @SerializedName("authenticationType")
//    var authenticationType: Any,
//    @SerializedName("authorizationKey")
//    var authorizationKey: Any,
//    @SerializedName("city")
//    var city: Any,
//    @SerializedName("clientToken")
//    var clientToken: Any,
    @SerializedName("email")
    var email: String,
    @SerializedName("firstName")
    var firstName: String,
//    @SerializedName("getNotifications")
//    var getNotifications: Boolean,
//    @SerializedName("groupName")
//    var groupName: Any,
//    @SerializedName("isBlacklisted")
//    var isBlacklisted: Boolean,
//    @SerializedName("isEmailVerified")
//    var isEmailVerified: Boolean,
//    @SerializedName("lastName")
//    var lastName: Any,
//    @SerializedName("password")
//    var password: Any,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("publicKey")
    var publicKey: String,
//    @SerializedName("sourceClientID")
//    var sourceClientID: Int,
//    @SerializedName("st")
//    var st: Any,
//    @SerializedName("streetAddress")
//    var streetAddress: Any,
//    @SerializedName("zip")
//    var zip: Any
)