package com.noion.randome.domain.randomchat


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.noion.randome.R
import com.noion.randome.databinding.ActivityRandomChatBinding
import java.lang.ref.WeakReference


//앞서 정의한 RandomChatNavigator 인터페이스를 구현하고 RandomChatViewModel에 WeakReference로 삽입해준다.
class RandomChatActivity: AppCompatActivity(), RandomChatNavigator {

    val viewModel by lazy {
        ViewModelProvider(this)
                .get(RandomChatViewModel::class.java).also {
                    it.navigatorRef = WeakReference(this)
                }
    }

    val binding by lazy {
        DataBindingUtil
                .setContentView<ActivityRandomChatBinding>(
                        this,
                        R.layout.activity_random_chat
                )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner =this

        //onCreate 함수 내에서 어댑터를 생성하고 연결
        setupConversationAdapter()
    }

    private fun setupConversationAdapter() {
        binding.conversation.layoutManager = LinearLayoutManager(this)
        binding.conversation.adapter =
                ConversationAdapter(this).also {
                    //앞서 준비한 addMessages 함수를 호출해 뷰모델이 가지고 있던 메세지들을 추가
                    it.addMessages(viewModel.messages)
        }
    }

    override fun onMessage(messageModel: MessageModel) {
        //메세지가 추가되면 ConversationAdapter의 addMessage를 호출해 UI에 메세지를 추가했다.
        //메세지가 추가되면 추가된 메세지가 화면에 보일 수 있도록
        // RecyclerView의 smoothScrollPosition을 마지막에 호출해 가장 아래로 스크롤 되도록 함
        val adapter = binding.conversation.adapter as? ConversationAdapter

        adapter?.let {
            adapter.addMessage(messageModel)

            val lastPosition = adapter.itemCount - 1
            binding.conversation.smoothScrollToPosition(lastPosition)
        }
    }
 }