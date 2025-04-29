package com.myoutdoor.agent.models.licensedetails.formylicense.license

data class VehicleDetails(
    var licenseContractId: Int,
    var vehicleColor: String,
    var vehicleDetailID: Int,
    var vehicleLicensePlate: String,
    var vehicleMake: String,
    var vehicleModel: String,
    var vehicleState: String,
    var vehicleType: Any
)