package com.jay.baytalk.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.jay.baytalk.model.data.ChatRoom

object ChatRoomDiffUtil : DiffUtil.ItemCallback<ChatRoom>() {
    override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
        return oldItem == newItem
    }
}