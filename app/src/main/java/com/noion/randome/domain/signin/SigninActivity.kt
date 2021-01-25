package com.noion.randome.domain.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.noion.randome.R
import com.noion.randome.api.response.ApiResponse
import com.noion.randome.api.response.SigninResponse
import com.noion.randome.databinding.ActivitySigninBinding
import com.noion.randome.domain.Auth
import java.lang.ref.WeakReference

class SigninActivity : AppCompatActivity(), SigninNavigator {
    private val viewModel by lazy { //get 함수를 이용해 뷰모델 생성, 이미 생성된 뷰모델이 존재하는 경우 해당 뷰모델 반환
        ViewModelProvider(this).get(SigninViewModel::class.java).also {
            it.navigatorRef = WeakReference(this)
        }
    }

    private val binding by lazy { //DatabindingUtil.setContentView 함수로 컨텐트 뷰를 설정하면 라이브러리가
        // 자동으로 생성해준 데이터 바인딩 클래스의 객체를 반환한다. findViewByUd 같은 함수를 호출하지 않고도 직접 접근 가능
        DataBindingUtil.setContentView<ActivitySigninBinding>(
                this, R.layout.activity_signin
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.signout() //앱이 비정상 종료 되었을 때 액티비티 생성시 Auth.signout을 호출한다.
        //서버에서 기존 접속 세션을 데이터베이스 등에 유지할 거라면 굳이 필요하지 않은 코드이지만,
        //웹소켓 접속이 끊긴 경우 랜덤 채팅 세션에서 제거 되도록 구현하였으므로
        //앱이 시작될 때에 사용자 정보가 초기화 되도록 구현

        binding.viewModel = viewModel
        binding.lifecycleOwner = this //바인딩 객체의 lifecycleOwner를 설정해주지 않았다면 뷰모델과 뷰의 변경이 서로 반영되지 않게 된다.
    }

    override fun startRandomChatActivity(response: ApiResponse<SigninResponse>) {
        finish()
    }
}