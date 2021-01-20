package com.noion.randome.common

import android.preference.PreferenceManager
import com.noion.randome.App

//API 토큰 및 사용자의 일부 정보를 저장하고 관리할 클래스
//간결하게 API 토큰들과 닉네임만 사용하도록 함

object Prefs {
    private const val TOKEN = "token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val USER_NAME = "nick_name"

    val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.instance)
    }

    var token
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit()
            .putString(TOKEN, value)
            .apply()

    var refreshToken
        get() = prefs.getString(REFRESH_TOKEN,null)
        set(value) = prefs.edit()
            .putString(REFRESH_TOKEN, value)
            .apply()

    var nickName
        get() = prefs.getString(USER_NAME, null)
        set(value) = prefs.edit()
            .putString(USER_NAME, value)
            .apply()
}