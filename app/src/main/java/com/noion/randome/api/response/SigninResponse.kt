package com.noion.randome.api.response

//로그인 응답
data class SigninResponse(
    val token: String,
    val refreshToken: String,
    val nickName: String
)