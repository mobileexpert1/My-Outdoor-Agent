package com.myoutdoor.agent.fragment.verification.code.body

data class UpdatePasswordBody(
    var Password:String,
    var PublicKey:String,
    var Token:String,
)