package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class ClientDocument(
    @SerializedName("clienDocxID")
    var clienDocxID: Any,
    @SerializedName("clientDocument")
    var clientDocument: Any,
    @SerializedName("clientDocumentID")
    var clientDocumentID: Int,
    @SerializedName("clientID")
    var clientID: Int,
    @SerializedName("dayPass")
    var dayPass: Boolean,
    @SerializedName("documentName")
    var documentName: String,
    @SerializedName("fileName")
    var fileName: String,
    @SerializedName("isPublic")
    var isPublic: Boolean,
    @SerializedName("permit")
    var permit: Boolean,
    @SerializedName("rlu")
    var rlu: Boolean,
    @SerializedName("uploadFile")
    var uploadFile: Any
)