package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName
import com.myoutdoor.agent.fragment.licence.RenewMembers
import com.myoutdoor.agent.fragment.licence.model.SpecialCondition

data class Model(
    @SerializedName("activityDetail")
    var activityDetail: ActivityDetail,
    @SerializedName("activityDetailPageChecks")
    var activityDetailPageChecks: ActivityDetailPageChecks,
    @SerializedName("amenities")
    var amenities: List<Amenity>,
    @SerializedName("clientDetails")
    var clientDetails: ClientDetails,
    @SerializedName("clientDocuments")
    var clientDocuments: List<ClientDocuments>,
    @SerializedName("images")
    var images: List<Image>,
    @SerializedName("mapFiles")
    var mapFiles: Any,
    @SerializedName("members")
    var members: List<RenewMembers>,
    @SerializedName("similarProperties")
    var similarProperties: List<SimilarProperty>,
    @SerializedName("specialConditions")
    var specialConditions: List<SpecialCondition>
)