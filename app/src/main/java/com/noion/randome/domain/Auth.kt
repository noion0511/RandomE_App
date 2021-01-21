package com.noion.randome.domain

import com.noion.randome.common.Prefs

//로그인, 로그아웃에 필요한 토큰 저장, 삭제 등의 처리를 담당하는 클래스

object Auth {

    fun signin(token: String, refreshToken: String, nickName: String) {
        Prefs.token = token
        Prefs.refreshToken = refreshToken
        Prefs.nickName = nickName
    }

    fun signout() {
        Prefs.token = null
        Prefs.refreshToken = null
        Prefs.nickName = null
    }

    fun refreshToken(token: String) {
        Prefs.token = token
    }
}