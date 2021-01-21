package com.noion.randome.api

import com.noion.randome.common.Prefs
import com.noion.randome.common.clearTasksAndStartNewActivity
import com.noion.randome.domain.Auth
import com.noion.randome.domain.signin.SigninActivity
import okhttp3.Interceptor
import okhttp3.Response

//refreshTokenAPI를 호출할 때 헤더에 리프레쉬 토큰을 넣어주는 기능

class ApiTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().apply {
            Prefs.refreshToken?.let {
                header("Authorization", it)
            }
            method(original.method(), original.body())
        }.build()

        val response = chain.proceed(request)

        if(response.code() == 401) {
            Auth.signout()
            clearTasksAndStartNewActivity<SigninActivity>()
        }

        return response
    }
}