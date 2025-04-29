package com.myoutdoor.agent.models.search

data class SearchBody(
    var Amenities: List<Any>,
    var Client: String,
    var County: List<Any>,
    var IPAddress: String,
    var Order: String,
    var PriceMax: String,
    var PriceMin: String,
    var ProductTypeID: String,
    var PropertyName: List<Any>,
    var RLU: List<Any>,
    var RLUAcresMax: String,
    var RLUAcresMin: String,
    var Sort: String,
    var RegionName: List<Any>,
    var StateName: List<Any>,
    var UserAccountID: String,
    var freeText: List<Any>
)

