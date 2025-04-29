package com.myoutdoor.agent.models.point_layer

data class PointLayerResponse(
    val features: List<Feature>,
    val type: String
)