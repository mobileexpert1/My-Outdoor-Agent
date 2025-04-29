package com.myoutdoor.agent.models.getPaymentToken

data class GetPaymentTokenBody(
    var cancelUrl: String,
    var clientInvoiceId: Int,
    var email: String,
    var errorUrl: String,
    var fundAccountKey: String,
    var licenseFee: Double,
    var paidBy: String,
    var productID: Int,
    var productTypeId: Int,
    var requestType: String,
    var returnUrl: String,
    var rluNo: String? =null,
    var userAccountId: Int,
    var invoiceTypeID:Int
)