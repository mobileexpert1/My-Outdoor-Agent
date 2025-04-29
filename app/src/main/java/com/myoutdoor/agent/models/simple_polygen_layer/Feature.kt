package com.myoutdoor.agent.models.simple_polygen_layer

data class Feature(
    val geometry: Geometry,
    val id: Int,
    val properties: Properties,
    val type: String
)