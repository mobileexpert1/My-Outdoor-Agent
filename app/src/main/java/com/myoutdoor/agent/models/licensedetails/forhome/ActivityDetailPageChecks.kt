package com.myoutdoor.agent.models.licensedetails.forhome

data class ActivityDetailPageChecks(
    var existingLicenseHolderAccountID: Int,
    var existingLicenseHolderProfileID: Int,
    var isFreeAccessPermit: Boolean,
    var isPreApprovalRequested: Boolean,
    var isRenewal: Boolean,
    var isUserProfileComplete: Boolean,
    var preApprRequestID: Int,
    var preApprovalStatus: Any,
    var showAlreadyPurchasedButton: Boolean,
    var showComingSoonButton: Boolean,
    var showLicenseFee: Boolean,
    var showLicenseNowButton: Boolean,
    var showPreApprovalRequestButton: Boolean,
    var showRenewButton: Boolean,
    var showRequestEntry: Boolean,
    var showSoldOutButton: Boolean
)