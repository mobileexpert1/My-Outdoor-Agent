package com.myoutdoor.agent.models.preapprovalrequest

data class Data(
    var acres: Double,
    var requestType: String,
    var countyName: String,
    var dateSubmitted: String,
    var displayName: String,
    var imageFilename: String,
    var licenseActivityID: Int,
    var licenseEndDate: String,
    var licenseFee: Double,
    var licenseStartDate: String,
    var licenseStatus: Any,
    var preApprRequestID: String,
    var productID: Int,
    var productNo: String,
    var propertyName: String,
    var preSaleToken:String,
    var publicKey: String,
    var requestStatus: String,
    var stateName: String,
    var email:String
)

