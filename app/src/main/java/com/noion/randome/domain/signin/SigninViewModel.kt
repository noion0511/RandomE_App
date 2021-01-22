package com.noion.randome.domain.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.noion.randome.api.RandomChatApi
import com.noion.randome.api.response.ApiResponse
import com.noion.randome.api.response.SigninResponse
import com.noion.randome.domain.Auth
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.lang.ref.WeakReference

class SigninViewModel(app: Application) : AndroidViewModel(app) {

    //뷰모델 클래스의 프로퍼티로 SigninNavigator를 WeakReference로 감싼다.
    //이렇게 참조된 SigninNavigator 객체는 WeakReference 이외의 객체 참조가 없다면
    //GC 대상이 되어 컨텍스트를 들고 있더라도 메모리릭을 일으키지 않는다.
    var navigatorRef: WeakReference<SigninNavigator>? = null
    private val navigator get() = navigatorRef?.get()
    var nickname = MutableLiveData("") //데이터바인딩 라이브러리에서도 프로퍼티를 MutableLiveData 타입으로 선언하면 UI코드에서 동적으로 사용될 수 있는 듯?

    fun signIn() = viewModelScope.launch { //랜덤채팅에 입장하는 함수로, 닉네임이 비었는지 검사하고
        nickname.value?.let {                    //signin API를 호출하는 단순한 구조, sunCatching을 사용해 결과를 처리하는 코드를 분리
            runCatching {
                validateNickName(it)
                RandomChatApi.signin(it)
            }.onSuccess { response ->
                handleSignin(response)
            }.onFailure { e ->
                Log.e("SigninViewModel", "sign-in failure.", e)
                toast(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    private fun validateNickName(nickName: String) {
        require(nickName.trim().isNotEmpty()) {
            "닉네임 형식이 잘못되었습니다."
        }
    }

    private fun handleSignin(response: ApiResponse<SigninResponse>) { //랜덤채팅 입장요청 결과를 처리하는 함수
        if(response.success && response.data != null) {               //입장이 성공적으로 이루어진 경우-> auth 클래스를 통해 로그인 처리
            val signin = response.data

            Auth.signin(signin.token,
                        signin.refreshToken,
                        signin.nickName)

            navigator?.startRandomChatActivity(response)
        } else {
            toast(response.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}