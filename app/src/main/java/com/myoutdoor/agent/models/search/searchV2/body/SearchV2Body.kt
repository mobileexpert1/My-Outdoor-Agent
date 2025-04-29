package com.myoutdoor.agent.models.search.searchV2.body


import com.google.gson.annotations.SerializedName

data class SearchV2Body(
    @SerializedName("Amenities")
    var amenities: List<Any>,
    @SerializedName("Client")
    var client: String,
    @SerializedName("County")
    var county: List<Any>,
    @SerializedName("Order")
    var order: String,
    @SerializedName("PageNumber")
    var pageNumber: Int,
    @SerializedName("Product")
    var product: List<Any>,
    @SerializedName("ProductAcresMax")
    var productAcresMax: Int,
    @SerializedName("ProductAcresMin")
    var productAcresMin: Int,
    @SerializedName("ProductPriceMax")
    var productPriceMax: Int,
    @SerializedName("ProductPriceMin")
    var productPriceMin: Int,
    @SerializedName("ProductTypeID")
    var productTypeID: Int,
    @SerializedName("Sort")
    var sort: String,
    @SerializedName("State")
    var state: List<Any>
)