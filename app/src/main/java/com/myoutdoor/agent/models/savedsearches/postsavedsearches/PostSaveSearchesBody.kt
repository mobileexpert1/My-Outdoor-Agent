package com.myoutdoor.agent.models.savedsearches.postsavedsearches

data class PostSaveSearchesBody(
    var Amenities: List<Any>,
    var Client: String,
    var County: List<Any>,
    var IPAddress: String,
    var PriceMax: String,
    var PriceMin: String,
    var ProductTypeID: String,
    var PropertyName: List<Any>,
    var RLU: List<Any>,
    var RLUAcresMax: String,
    var RLUAcresMin: String,
    var RegionName: List<Any>,
    var SearchName: String,
    var StateName: List<Any>,
    var UserAccountID: String,
    var freeText: List<Any>
)