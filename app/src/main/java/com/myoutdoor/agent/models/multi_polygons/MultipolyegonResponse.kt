package com.myoutdoor.agent.models.multi_polygons

data class MultipolyegonResponse(
    val features: List<Feature>,
    val type: String
)