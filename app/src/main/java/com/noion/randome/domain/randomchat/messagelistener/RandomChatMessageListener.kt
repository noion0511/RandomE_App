package com.noion.randome.domain.randomchat.messagelistener

// 웹소켓과 관련된 이벤트 리스너
interface RandomChatMessageListener {
    //서버로부터 정상적인 웹소켓 메세지를 받았을 때 호출될 함수
    fun onMessage(message: Message)

    //서버로부터 받은 웹소켓 메세지의 형식 모류 등에서 발생할 수 있는 메세지 처리 오류시 호출될 함수
    fun onMessageError(t: Throwable)

    //서버 접속 오류등의 내트워크 오류 상황에서 호출될 함수
    fun onNetworkError(t: Throwable)

    //웹소켓 세션이 시작되었을 때 호출될 함수
    fun onStart()

    //웹소켓 접속이 끊겼을 때 호출될 함수
    fun onClosed()
}