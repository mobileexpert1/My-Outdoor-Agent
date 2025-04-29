package com.myoutdoor.agent.models.licence.newModel.response

import com.google.gson.annotations.SerializedName

data class ClientDocuments(
    @SerializedName("clientDocumentID")
    var clientDocumentID: Int,
    @SerializedName("clientID")
    var clientID: Int,
    @SerializedName("clientDocument")
    var clientDocument: Any,
    @SerializedName("isPublic")
    var isPublic: Boolean,
    @SerializedName("rlu")
    var rlu: Boolean,
    @SerializedName("permit")
    var permit: Boolean,
    @SerializedName("dayPass")
    var dayPass: Boolean,
    @SerializedName("fileName")
    var fileName: String,
    @SerializedName("uploadFile")
    var uploadFile: Any,
    @SerializedName("clienDocxID")
    var clienDocxID: Any,
    @SerializedName("documentName")
    var documentName: String

)
