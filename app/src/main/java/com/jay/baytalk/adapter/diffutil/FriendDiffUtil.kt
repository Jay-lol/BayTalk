package com.jay.baytalk.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.jay.baytalk.model.data.Friend

object FriendDiffUtil : DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.rid == newItem.rid
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem == newItem
    }
}