package com.noion.randome.api.response

data class SigninResponse(
    val token: String,
    val refreshToken: String,
    val nickName: String
)