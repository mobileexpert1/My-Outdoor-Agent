package com.myoutdoor.agent.models.getallstates

data class Model(
    var countyID: Int,
    var countyName: Any,
    var countyNameLSAD: Any,
    var countyNames: Any,
    var ctfips: Any,
    var regionName: Any,
    var state: String,
    var stateAbbrev: String,
    var stateName: String
)