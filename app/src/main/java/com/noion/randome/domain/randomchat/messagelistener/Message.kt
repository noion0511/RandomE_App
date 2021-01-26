package com.noion.randome.domain.randomchat.messagelistener

data class Message( //웹소켓 서버로 부터 받을 메세지?
        val senderNickName: String,
        val message: String
)