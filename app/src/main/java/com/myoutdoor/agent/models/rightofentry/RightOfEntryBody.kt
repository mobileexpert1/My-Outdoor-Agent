package com.myoutdoor.agent.models.rightofentry

data class RightOfEntryBody(
    var Address: String,
    var County: String,
    var DateOfAccessRequested: String,
    var Permittee: String,
    var ProductID: String,
    var ProductNo: String,
    var RoERequestID: String,
    var RoEUsersLists: String,
    var State: String,
    var TractName: String,
    var UserAccountID: String,
    var UserName: String
)