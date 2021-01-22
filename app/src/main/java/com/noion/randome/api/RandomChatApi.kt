package com.noion.randome.api

import com.noion.randome.api.request.MessageRequest
import com.noion.randome.api.response.ApiResponse
import com.noion.randome.api.response.SigninResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

//사용할 인터페이스 정의, 토큰을 기준으로 일반 API, 토큰 갱신용 API 인터페이스를 구분
//RandomChatApi는 일반 Api -> 채팅에 진입하는 signin과 메세지를 전송하는 sendMessage
interface RandomChatApi {

    @POST("/api/v1/randomchat/signin")
    suspend fun signin(
            @Query("nickName") nickName: String
    ): ApiResponse<SigninResponse>

    @POST("/api/v1/randomchat/message")
    suspend fun sendMessage(
            @Body request: MessageRequest
    ): ApiResponse<Any>

    companion object {
        private val instance = ApiGenerator()
                .generate(RandomChatApi::class.java)


        suspend fun signin(nickName: String) =
                withContext(Dispatchers.IO) {
                    instance.signin(nickName)
                }

        suspend fun sendMessage(message: String) =
                withContext(Dispatchers.IO) {
                    instance.sendMessage(MessageRequest(message))
                }
    }
}