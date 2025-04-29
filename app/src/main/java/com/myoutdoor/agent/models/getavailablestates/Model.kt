package com.myoutdoor.agent.models.getavailablestates

data class Model(
    var countyID: Int,
    var countyName: String,
    var countyNameLSAD: Any,
    var countyNames: String,
    var ctfips: Any,
    var regionName: Any,
    var state: String,
    var stateAbbrev: String,
    var stateName: String
)