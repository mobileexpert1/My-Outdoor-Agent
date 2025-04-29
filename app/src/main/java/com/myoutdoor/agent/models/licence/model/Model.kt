package com.myoutdoor.agent.fragment.licence.model

import com.myoutdoor.agent.fragment.licence.RenewMembers

data class Model(
    val activityDetail: ActivityDetail,
    val activityDetailPageChecks: ActivityDetailPageChecks,
    val amenities: List<Amenity>,
    val clientDetails: ClientDetails,
    val images: List<Image>,
    val mapFiles: Any,
    val members: List<RenewMembers>,
    val similarProperties: List<SimilarProperty>,
    val specialConditions:  List<SpecialCondition>

)