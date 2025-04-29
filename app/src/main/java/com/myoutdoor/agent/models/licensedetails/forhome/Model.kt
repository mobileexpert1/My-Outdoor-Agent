package com.myoutdoor.agent.models.licensedetails.forhome

data class Model(
    var amenities: List<Amenity>,
    var images: List<Image>,
    var mapFiles: List<MapFile>,
    var specialConditions: List<SpecialCondition>,
    var activityDetail: ActivityDetail,

    var activityDetailPageChecks: ActivityDetailPageChecks,
    var clientDetails: ClientDetails,
    var members: Any,
    var similarProperties: List<SimilarProperty>

)