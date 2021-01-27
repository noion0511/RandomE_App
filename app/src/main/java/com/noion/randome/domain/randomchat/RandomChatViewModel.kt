package com.noion.randome.domain.randomchat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.noion.randome.api.RandomChatApi
import com.noion.randome.common.Prefs
import com.noion.randome.common.clearTasksAndStartNewActivity
import com.noion.randome.domain.Auth
import com.noion.randome.domain.randomchat.messagelistener.Message
import com.noion.randome.domain.randomchat.messagelistener.RandomChatMessageListener
import com.noion.randome.domain.randomchat.websocketclient.RandomChatWebSocketClient
import com.noion.randome.domain.signin.SigninActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.lang.ref.WeakReference


//액티비티와 관계없이 웹소켓 세션을 유지하기 위해 RandomChatViewModel에서 RandomChatWebSocketClient를 관리한다.
//RandomChatWebSocketClient를 생성하기 위해 생성자에 RandomChatMessageListener을 넘겨줘야하기 때문에
//RandomChatViewModel이 이 리스너를 구현하고 웹소켓의 이벤트를 받아 처리한다.
class RandomChatViewModel(app: Application) : AndroidViewModel(app), RandomChatMessageListener {

    //RandomChatWebSocketClient를 생성해 멤버 프로퍼티로 관리한다.
    private val client = RandomChatWebSocketClient(this)

    var navigatorRef: WeakReference<RandomChatNavigator>? = null

    private val navigator get() = navigatorRef?.get()

    //사용자 메세지 입력 뷰에 바인딩 될 텍스트 프로퍼티
    val inputMessage = MutableLiveData("")

    //사용자가 주고받은 메세지를 저장할 리스트
    //보통은 메세지를 로컬 데이터베이스에 저장하고관리하는 경우가 많지만 이 앱에서는
    //랜덤채팅 기록을 저장할 필요가 없으므로 메모리의 변수로 관리한다.
    val messages = mutableListOf<MessageModel>()

    //메세지가 추가되었을 떄 호출할 함수
    //메세지의 소유자 정보와 메세지 객체를 받아 MessageModel 객체를 생성하고
    //최근 메세지와 비교해 collapseName 프로퍼티의 값을 결정한 귀 message에 메세지를 추가한다.
    //리싸이클러뷰에 데이터를 추가하기 위해 RandomChatNavigator의 onMessage()함수 호출
    private fun handleMessage(
            owner: MessageModel.Owner,
            message: Message
    ) {
        synchronized(messages) {
            val messageModel = MessageModel(
                    owner,
                    message.senderNickName,
                    message.message
            )

            messages.lastOrNull()?.let { lastMessage ->
                messageModel.collapseName =
                        lastMessage.nickName == messageModel.nickName &&
                                lastMessage.owner == messageModel.owner
            }

            messages.add(messageModel)

            viewModelScope.launch(Dispatchers.Main) {
                navigator?.onMessage(messageModel)
            }
        }
    }

    //사용자가 입력한 텍스트를 전송할 때 호출되는 함수
    //입력된 텍스트가 빈 값인지 간단히 검사한 뒤, 사용자 입력 필드를 비워주고
    //서버의 sendMessage API를 호출해 메세지를 전송,
    // 성공적으로 전송 후 handleMessage 함수를 호출해 전송한 메세지를 messages 리스트에 추가
    fun sendMessage() = viewModelScope.launch {
        inputMessage.value?.let { content ->
            if(content.isEmpty() || content.isBlank())
                return@launch

            inputMessage.value = ""

            runCatching {
                RandomChatApi.sendMessage(content)

                Prefs.nickName?.let { nickName ->
                    val message = Message(nickName, content)
                    handleMessage(MessageModel.Owner.SENDER, message)
                }
            }.onFailure {
                onMessageError(it)
            }
        }
    }

    //이 아래는 RandomChatMessageListener 인터페이스의 구현
    //메세지를 수신했을 때, handleMessage를 호출했을 때,
    // 받은 메세지를 리싸이클러 뷰에서 보여줄 수 있도록 처리
    override fun onMessage(message: Message) {
        handleMessage(MessageModel.Owner.RECEIVER, message)
    }

    override fun onMessageError(t: Throwable) {
        Log.e(TAG, "메세지 오류 발생", t)
        toast("메세지 오류가 발생했습니다.")
    }

    override fun onNetworkError(t: Throwable) {
        Log.e(TAG, "네트워크 오류 발생", t)
        toast("네트워크 오류가 발생했습니다.")

        Auth.signout() //로그아웃 뒤 첫화면으로
        clearTasksAndStartNewActivity<SigninActivity>()
    }

    override fun onStart() {
        Log.d(TAG, "채팅 시작")
    }

    override fun onClosed() {
        Auth.signout() //로그아웃 뒤 첫화면으로
        clearTasksAndStartNewActivity<SigninActivity>()
    }

    companion object {
        const val TAG = "RandomChatViewModel"
    }
}