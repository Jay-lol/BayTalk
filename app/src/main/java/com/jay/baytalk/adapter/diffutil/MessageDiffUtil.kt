package com.jay.baytalk.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.jay.baytalk.model.data.MessageData

object MessageDiffUtil : DiffUtil.ItemCallback<MessageData>() {
    override fun areItemsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
        return oldItem.time == newItem.time && oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
        return oldItem == newItem
    }
}