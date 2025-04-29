package com.myoutdoor.agent.fragment.message_new.model.get_messages

data class Model(
    val adMsgID: Int,
    val adminAccountID: Int,
    val adminFirstName: Any,
    val adminInitials: Any,
    val adminLastName: Any,
    val attachments: Any,
    val dateLastLogin: String,
    val firstName: String,
    val initials: String,
    val lastName: String,
    val messageText: String,
    val postedDate: String,
    val productID: Int,
    val productNo: Any,
    val publicKey: String,
    val status: String,
    val userAccountID: Int,
    val userMsgID: Int,
    val userProfileID: Int,
    val userType: String
)