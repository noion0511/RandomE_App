package com.noion.randome.domain.signin

import com.noion.randome.api.response.ApiResponse
import com.noion.randome.api.response.SigninResponse

//AnkoMVVM을 사용하지 않으므로 뷰모델에서 startActivity 등의 컨텍스트 함수를 사용할 수가 없는 문제가 있다.
//그래서 필요한 컨텍스트 함수를 사용할 수 있도록 인터페이스를 정의
//Navigator는 MVVM 아키텍처 뷰모델이 사용할 컨텍스트 함수의 인터페이스를 정의할 때 나름대로 흔히 사용되는 네이밍 규칙이라고 한다..

interface SigninNavigator { //로그인이 성공적으로 이루어 졌을 때 호출될 채팅 액티비티를 시작하는 함수 정의
    fun startRandomChatActivity(response: ApiResponse<SigninResponse>)
}