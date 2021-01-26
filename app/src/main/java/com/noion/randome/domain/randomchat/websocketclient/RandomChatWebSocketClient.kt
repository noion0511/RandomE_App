package com.noion.randome.domain.randomchat.websocketclient

import android.util.Log
import com.google.gson.Gson
import com.noion.randome.App
import com.noion.randome.common.Prefs
import com.noion.randome.domain.randomchat.messagelistener.Message
import com.noion.randome.domain.randomchat.messagelistener.RandomChatMessageListener
import okhttp3.*

//웹소켓을 핸들링하는 코드, 웹소켓의 의존성을 최소화 하고자  웹소켓 관련 클래스에서 하는 일이 적음
//웹소켓 메세지를 받아 이 메세지를 파싱하고 뷰모델이 구현할 RandomChatMessageListener 함수를 호출해주는 역할

class RandomChatWebSocketClient(
        private val messageListener: RandomChatMessageListener
) : WebSocketListener() {

    //웹소켓 접속을 위한 okhttpclient 객체
    private val okHttpClient = OkHttpClient()
    //웹소켓으로부터 받은 json 메세지를 파싱할 객체
    private val gson = Gson()

    init {
        //웹소켓 연결시 헤더에 넣어주기 위한 토큰 문자열
        val accessToken = "$ACCESS_TOKEN ${Prefs.token}"

        //헤더에 앞서 선언한 토큰 문자열을 삽입하고 웹소켓 서버 주소를 지정해 연결 요청 객체를 생성
        val request = Request.Builder()
                .addHeader(SEC_PROTOCOL, accessToken)
                .url(App.WEBSOCKET_ENDPOINT)
                .build()

        //okhttpclient 객체를 통해 앞서 생성한 요청 객체와 웹소켓 이벤트 리스너를 파라미터로 웹소켓 연결을 요청
        //연결이 성공적으로 이루어지면 리스너의 onOpen 콜백이 호출
        okHttpClient.newWebSocket(request, this)
        okHttpClient.dispatcher().executorService().shutdown()
    }


    //onMessage는 웹소켓으로부터 메세지가 도착했을 때 호출되는 콜백 함수
    //json 객체를 통해 웹소켓 메세지를 우리가 앞서 정의한 Message 객체로 파싱하며 메세지가 정상적으로
    //파싱 되었을 때, RandomChatMessageListener의 onMessage 함수를 호출
    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("RandomChatWebSocket", "message: $text")
        val message = runCatching {
            gson.fromJson(text, Message::class.java)
        }

        message.onSuccess { messageListener.onMessage(it) }
        message.onFailure { messageListener.onMessageError(it) }
    }

    //웹소켓 접속이 실패 했을 경우, 호출되는 콜백
    //RandomChatMessageListener의 onNetworkError 함수를 호출한 후 소켓 연결을 닫음
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        messageListener.onNetworkError(t)
        webSocket.close(-1, "")
    }

    //웹소켓 연결이 정상적으로 이루어 졌을때, 호출되는 콜백
    //RandomChatMessageListener의 onStart 함수 호출
    override fun onOpen(webSocket: WebSocket, response: Response) {
        messageListener.onStart()
    }

    //웹소켓 연결이 끊어졌을 때, 호출되는 콜백
    //RandomChatMessageListener의 onClosed를 호출
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        messageListener.onClosed()
    }

    companion object {
        const val SEC_PROTOCOL = "sec-websocket-protocol"
        const val ACCESS_TOKEN = "access_token"
    }
}