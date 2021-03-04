package com.jay.baytalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.adapter.diffutil.ChatRoomDiffUtil
import com.jay.baytalk.databinding.RecyclerChatroomBinding
import com.jay.baytalk.model.data.ChatRoom
import kotlinx.android.synthetic.main.recycler_chatroom.view.*

class RecyclerChatRoomListAdapter(listner : OnItemClick)
    : ListAdapter<ChatRoom, RecyclerChatRoomListAdapter.ChatViewHolder>(ChatRoomDiffUtil){

    private val callback = listner

    inner class ChatViewHolder(val view: RecyclerChatroomBinding) : RecyclerView.ViewHolder(view.root) {
        fun onBind(item: ChatRoom) {
            view.roomName.text = item.usersName
            view.roomMemo.text = item.lastMessage
            setButton(this , item.roomId, item.usersName, item.userUid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            RecyclerChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    private fun setButton(
        holder: ChatViewHolder,
        rid: String?,
        chatName: String,
        userUid: List<String>?
    ) {
        holder.itemView.setOnLongClickListener {
            if (rid!=null) callback.onChatRoomDelete(rid)
            true
        }

        holder.itemView.setOnClickListener {
            userUid?:return@setOnClickListener
            callback.onChatroomClick(rid!!, chatName, userUid)
        }
    }
}