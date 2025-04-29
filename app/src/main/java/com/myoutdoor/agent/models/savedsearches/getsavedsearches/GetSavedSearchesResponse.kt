package com.myoutdoor.agent.models.savedsearches.getsavedsearches

import java.util.ArrayList

data class GetSavedSearchesResponse(
    var message: String,
    var model: ArrayList<Model>? = null,
    var statusCode: Int
)