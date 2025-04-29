package com.myoutdoor.agent.fragment.licence.model

data class ActivityDetailPageChecks(
    val existingLicenseHolderAccountID: Int,
    val existingLicenseHolderProfileID: Int,
    val isFreeAccessPermit: Boolean,
    val isPreApprovalRequested: Boolean,
    val isRenewal: Boolean,
    val isUserProfileComplete: Boolean,
    val preApprRequestID: Int,
    val preApprovalStatus: String,
    val showAlreadyPurchasedButton: Boolean,
    val showComingSoonButton: Boolean,
    val showLicenseFee: Boolean,
    val showLicenseNowButton: Boolean,
    val showPreApprovalRequestButton: Boolean,
    val showRenewButton: Boolean,
    val showRequestEntry: Boolean,
    val showSoldOutButton: Boolean
)