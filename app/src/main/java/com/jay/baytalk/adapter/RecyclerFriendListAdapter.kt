package com.jay.baytalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.adapter.diffutil.FriendDiffUtil
import com.jay.baytalk.databinding.RecyclerFriendBinding
import com.jay.baytalk.model.data.Friend

class RecyclerFriendListAdapter
    : ListAdapter<Friend, RecyclerFriendListAdapter.FriendViewHolder>(FriendDiffUtil) {

    inner class FriendViewHolder(val view: RecyclerFriendBinding) : RecyclerView.ViewHolder(view.root) {

        fun onBind(item: Friend, position: Int) {
            if (position==0)
                view.itsme.visibility = View.VISIBLE
            else
                view.itsme.visibility = View.GONE

            view.name.text = item.name
            view.email.text = item.memo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            RecyclerFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

}