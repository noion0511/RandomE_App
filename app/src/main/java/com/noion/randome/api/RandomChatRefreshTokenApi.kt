package com.noion.randome.api

import com.noion.randome.api.response.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.POST
import retrofit2.http.Query

//코튼 갱신용 api 인터페이스
interface RandomChatRefreshTokenApi {

    @POST("/api/v1/randomChat/refresh_token")
    suspend fun refreshToken(
            @Query("grant_type") grantType: String = "refresh_token"
    ): ApiResponse<String>

    companion object {
        private val instance = ApiGenerator()
                .generateRefreshClient(RandomChatRefreshTokenApi::class.java)

        suspend fun refreshToken() =
                withContext(Dispatchers.IO) {
                    instance.refreshToken()
                }
    }
}