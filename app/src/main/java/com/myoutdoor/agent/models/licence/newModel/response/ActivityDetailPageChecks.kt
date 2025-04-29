package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class ActivityDetailPageChecks(
    @SerializedName("existingLicenseHolderAccountID")
    var existingLicenseHolderAccountID: Int,
    @SerializedName("existingLicenseHolderProfileID")
    var existingLicenseHolderProfileID: Int,
    @SerializedName("isFreeAccessPermit")
    var isFreeAccessPermit: Boolean,
    @SerializedName("isPreApprovalRequested")
    var isPreApprovalRequested: Boolean,
    @SerializedName("isRenewal")
    var isRenewal: Boolean,
    @SerializedName("isUserProfileComplete")
    var isUserProfileComplete: Boolean,
    @SerializedName("preApprRequestID")
    var preApprRequestID: Int,
    @SerializedName("preApprovalStatus")
    var preApprovalStatus: Any,
    @SerializedName("showAcceptAndPayButton")
    var showAcceptAndPayButton: Boolean,
    @SerializedName("showAcceptButton")
    var showAcceptButton: Boolean,
    @SerializedName("showAlreadyPurchasedButton")
    var showAlreadyPurchasedButton: Boolean,
    @SerializedName("showComingSoonButton")
    var showComingSoonButton: Boolean,
    @SerializedName("showLicenseFee")
    var showLicenseFee: Boolean,
    @SerializedName("showLicenseNowButton")
    var showLicenseNowButton: Boolean,
    @SerializedName("showPreApprovalRequestButton")
    var showPreApprovalRequestButton: Boolean,
    @SerializedName("showRenewButton")
    var showRenewButton: Boolean,
    @SerializedName("showRequestEntry")
    var showRequestEntry: Boolean,
    @SerializedName("showSoldOutButton")
    var showSoldOutButton: Boolean
)