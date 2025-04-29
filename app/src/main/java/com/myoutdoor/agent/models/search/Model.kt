package com.myoutdoor.agent.models.search

import kotlinx.serialization.Serializable

@Serializable
data class Model(
    var activityType: Any,
    var amenityName: List<AmenityName>,
    var clientFeatures: Any,
    var licenseActivityID: Int,
    var licenseAgreement: Any,
    var mapFiles: Any,
    var productID: Int,
    var renewalActivity: Any,
    var rluImages: List<RluImage>,
    var rluPropertyModel: RluPropertyModel,
    var specCndDesc: String,
    var token: String,
    var userAccountID: Int
) : java.io.Serializable