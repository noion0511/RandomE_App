package com.noion.randome.api

import com.noion.randome.api.response.ApiResponse
import com.noion.randome.common.Prefs
import com.noion.randome.common.clearTasksAndStartNewActivity

import com.noion.randome.domain.Auth
import com.noion.randome.domain.signin.SigninActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

// API 호출시 토큰 만료로 인한 HPPT 401 응답이 떨어졌을 때 토큰을 갱신하고 API를 재요천 하도록 도와주는 함수

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code() == 401) {
            return runBlocking {
                val tokenResponse = refreshToken()

                handleTokenResponse(tokenResponse)

                Prefs.token?.let { token ->
                    response.request()
                        .newBuilder()
                        .header("Authorization", token)
                        .build()
                }
            }
        }
        return null
    }

    private suspend fun refreshToken() =
        try {
            RandomChatRefreshTokenApi.refreshToken()
        } catch (e: Exception) {
            ApiResponse.error<String>("인증 실패")
        }

    private fun handleTokenResponse(tokenResponse: ApiResponse<String>) {
        if(tokenResponse.succes && tokenResponse.data != null) {
            Auth.refreshToken(tokenResponse.data)
        } else {
            Auth.signout()
            clearTasksAndStartNewActivity<SigninActivity>()
        }
    }
}