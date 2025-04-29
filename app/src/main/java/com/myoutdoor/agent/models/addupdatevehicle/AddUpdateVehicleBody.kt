package com.myoutdoor.agent.models.addupdatevehicle

data class AddUpdateVehicleBody(
    var LicenseContractID: Int,
    var VehicleColor: String,
    var VehicleDetailID: String,
    var VehicleLicensePlate: String,
    var VehicleMake: String,
    var VehicleModel: String,
    var VehicleState: String,
    var VehicleType: String
)