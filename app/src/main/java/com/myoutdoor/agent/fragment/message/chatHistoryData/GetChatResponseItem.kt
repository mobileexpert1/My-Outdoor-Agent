package com.myoutdoor.agent.fragment.message.chatHistoryData

data class GetChatResponseItem(
    val adMsgID: Int,
    val adminAccountID: Int,
    val adminFirstName: String,
    val adminInitials: String,
    val adminLastName: String,
    val attachments: String,
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