package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.example.example.AdditionalInvoices
import com.google.gson.annotations.SerializedName
import com.myoutdoor.agent.models.licensedetails.formylicense.license.LicenseMember
import com.myoutdoor.agent.models.licensedetails.formylicense.license.VehicleDetails

data class Model(
    @SerializedName("acceptedBy")
    var acceptedBy: Any,
    @SerializedName("acceptedDate")
    var acceptedDate: String,
    @SerializedName("activityType")
    var activityType: Any,
    @SerializedName("additionalInvoices")
    var additionalInvoices: List<AdditionalInvoices>,
    @SerializedName("amenities")
    var amenities: List<Amenity>,
    @SerializedName("clientDocument")
    var clientDocument: List<ClientDocument>,
    @SerializedName("clientFeatures")
    var clientFeatures: ClientFeatures,
    @SerializedName("clubMemberDetails")
    var clubMemberDetails: List<Any>,
    @SerializedName("contractFiscalYear")
    var contractFiscalYear: Int,
    @SerializedName("contractStatus")
    var contractStatus: Any,
    @SerializedName("executedBy")
    var executedBy: Any,
    @SerializedName("executedDate")
    var executedDate: String,
    @SerializedName("licenseAcres")
    var licenseAcres: Double,
    @SerializedName("licenseAgreement")
    var licenseAgreement: String,
    @SerializedName("licenseContractID")
    var licenseContractID: Int,
    @SerializedName("licenseDetails")
    var licenseDetails: LicenseDetails,
    @SerializedName("licenseMembers")
    var licenseMembers: List<LicenseMember>,
    @SerializedName("licenseStatus")
    var licenseStatus: Any,
    @SerializedName("licenseType")
    var licenseType: Any,
    @SerializedName("mapFiles")
    var mapFiles: List<MapFile>,
    @SerializedName("memberDetailsModel")
    var memberDetailsModel: MemberDetailsModel,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productNo")
    var productNo: Any,
    @SerializedName("publicKey")
    var publicKey: Any,
    @SerializedName("renewalActivity")
    var renewalActivity: RenewalActivity,
    @SerializedName("specialConditions")
    var specialConditions: List<SpecialCondition>,
    @SerializedName("vehicleDetails")
    var vehicleDetails: List<VehicleDetails>
)