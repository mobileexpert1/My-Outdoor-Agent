package com.example.example

import com.google.gson.annotations.SerializedName


data class AdditionalInvoices(

    @SerializedName("adInvoiceID") var adInvoiceID: Int? = null,
    @SerializedName("displayName") var displayName: String? = null,
    @SerializedName("licenseHolderName") var licenseHolderName: String? = null,
    @SerializedName("typeName") var typeName: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("invoicePaid") var invoicePaid: Boolean? = null,
    @SerializedName("dateAdded") var dateAdded: String? = null,
    @SerializedName("productNo") var productNo: String? = null,
    @SerializedName("contractStatus") var contractStatus: String? = null,
    @SerializedName("invoiceTypeID") var invoiceTypeID: Int? = null,
    @SerializedName("paymentDate") var paymentDate: String? = null

)