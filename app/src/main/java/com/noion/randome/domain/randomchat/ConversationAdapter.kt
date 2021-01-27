package com.noion.randome.domain.randomchat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.noion.randome.R
import com.noion.randome.databinding.MessageByReceiverBinding
import com.noion.randome.databinding.MessageBySenderBinding

class ConversationAdapter(private val context:Context) : RecyclerView.Adapter<ConversationAdapter.MessageBoxViewHolder>() {
    //리싸이클러뷰에 보여질 메세지 데이터를 관리하는 프로퍼티
    private val messages = mutableListOf<MessageModel>()

    //addMessages 함수는 UI와 뷰모델의 라이프 사이클은 서로 다르기 때문에 UI가 초기화 되었을 때
    //뷰모델이 가지고 있는 기존의 메세지들을 추가해주기 위한 함수
    fun addMessages(messages: List<MessageModel>) {
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

    //UI 상에 메세지 한건을 추가하기 위한 함수
    fun addMessage(messageModel: MessageModel) {
        synchronized(messageModel) {
            messages.add(messageModel)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageBoxViewHolder {
        //리싸이클러 뷰가 새 뷰홀더를 요청했을 때 먼저 뷰 타입 번호에 따라 레이아수을 결정한다.
        val layout = when (viewType) {
            MessageModel.Owner.SENDER.viewType -> {
                R.layout.message_by_sender
            }
            else -> {
                R.layout.message_by_receiver
            }
        }

        //메세지모델의 데이터를 바인딩하기 위해 dataBindingUtil 클래스를 이요해 바인딩 클래스를 생성
        //이 바인딩 클래스는 MessageBoxViewHolder 클래스의 프로퍼티로 가지고 있는다.
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            layout,
            parent,
            false
        )

        return MessageBoxViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageBoxViewHolder, position: Int) {
        //리싸이클러뷰가 특정 영역의 데이터를 표시해주려 할 때
        //메세지의 소유자에 따라 뷰홀더의 바인딩 프로퍼티를 레이아웃에 맞는 바인딩 클래스로 캐스팅
        //MessageModel을 삽입
        val message = messages[position]

        when(message.owner) {
            MessageModel.Owner.SENDER -> {
                val binding = holder.binding as MessageBySenderBinding
                binding.message = message
            }
            MessageModel.Owner.RECEIVER -> {
                val binding = holder.binding as MessageByReceiverBinding
                binding.message = message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].owner.viewType
    }

    class MessageBoxViewHolder(val binding : ViewDataBinding) :
            RecyclerView.ViewHolder(binding.root)
}