package com.myoutdoor.agent.models.search

data class SearchResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)