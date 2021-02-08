package com.noion.randome

import android.app.Application

//전역 어플리케이션 컨텍스트를 사용하기 위해, API 서버 주소와 같은 일부 설정

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        const val HOST = "172.18.102.124"
        const val API_HOST = "http://$HOST"
        const val API_PORT = 8080
        const val WEBSOCKET_ENDPOINT = "ws://$HOST:8080/ws/randomchat"
    }
}