package com.myoutdoor.agent.models.licensedetails.formylicense.license

import com.example.example.AdditionalInvoices
import com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.ClientDocument

data class Model(
    var acceptedBy: Any,
    var acceptedDate: String,
    var activityType: Any,
    var additionalInvoices: ArrayList<AdditionalInvoices>,
    var amenities: List<Amenity>,
    var clientDocument: List<ClientDocument>,
    var clientFeatures: ClientFeatures,
    var clubMemberDetails: List<ClubMemberDetail>,
    var contractFiscalYear: Int,
    var contractStatus: Any,
    var executedBy: Any,
    var executedDate: String,
    var licenseAcres: Double,
    var licenseAgreement: String,
    var licenseContractID: Int,
    var licenseDetails: LicenseDetails,
    var licenseMembers: List<LicenseMember>,
    var licenseStatus: Any,
    var licenseType: Any,
    var mapFiles: List<MapFile>,
    var memberDetailsModel: MemberDetailsModel,
    var productID: Int,
    var productNo: String,
    var publicKey: String,
    var renewalActivity: RenewalActivity,
    var specialConditions: List<SpecialCondition>,
    var vehicleDetails: List<VehicleDetails>
)