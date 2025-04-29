package com.myoutdoor.agent.models.savedsearches.getsavedsearches

import kotlin.collections.ArrayList

data class Model(
    var Amenities:  ArrayList<String>,
    var Client: Any,
    var FreeText: Any,
    var IPAddress: Any,
    var PriceMax: Double,
    var PriceMin: Double,
    var PropertyName:  ArrayList<String>,
    var RLU: Any,
    var RLUAcresMax: Double,
    var RLUAcresMin: Double,
    var RegionName: ArrayList<String>,
    var SearchName: String,
    var StateName:  ArrayList<String>,
    var UserAccountID: Int,
    var UserSearchID: Int,
    var county: Any,
    var order: Any,
    var productTypeID: Int,
    var sort: Any
)