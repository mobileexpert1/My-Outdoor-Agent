package com.myoutdoor.agent.models.savedsearches.searchautofill

import java.util.ArrayList

data class SearchAutoFillResponse(
    var message: String,
    var model: ArrayList<Model>,
    var statusCode: Int
)