package com.myoutdoor.agent.fragment.message_new.model.refresh_messages

data class Model(
    var adMsgID: Int,
    var adminAccountID: Int,
    var adminFirstName: Any,
    var adminInitials: Any,
    var adminLastName: Any,
    var attachments: Any,
    var dateLastLogin: String,
    var firstName: String,
    var initials: String,
    var lastName: String,
    var messageText: String,
    var postedDate: String,
    var productID: Int,
    var productNo: Any,
    var publicKey: String,
    var status: String,
    var userAccountID: Int,
    var userMsgID: Int,
    var userProfileID: Int,
    var userType: String
)