package com.noion.randome.domain.randomchat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ConversationAdapter(private val context:Context) : RecyclerView.Adapter<ConversationAdapter.MessageBoxViewHolder>() {
    private val messages = mutableListOf<MessageModel>()

    fun addMessages(messages: List<MessageModel>) {
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

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
        val layout = when (viewType) {
            MessageModel.Owner.SENDER.viewType -> {
                R.layout.message_by_sender
            }
            else -> {
                R.layout.message_by_receiver
            }
        }

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