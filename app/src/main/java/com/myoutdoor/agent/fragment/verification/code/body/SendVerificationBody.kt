package com.myoutdoor.agent.fragment.verification.code.body

data class SendVerificationBody(
    var Type:String,
    var Email:String,
    var Phone:String,
    var Name:String,
    var PublicKey:String,
)


