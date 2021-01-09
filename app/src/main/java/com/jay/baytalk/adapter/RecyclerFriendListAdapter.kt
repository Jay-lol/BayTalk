package com.jay.baytalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.R
import com.jay.baytalk.model.data.Friend
import kotlinx.android.synthetic.main.recycler_friend.view.*


class RecyclerFriendListAdapter(fList : List<Friend>?)
    : RecyclerView.Adapter<RecyclerFriendListAdapter.FriendViewHolder>() {

    private var flist = fList

    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rid : Int? = null
        var name = view.name
        var email = view.email
        var isMe = view.itsme
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_friend, parent, false))
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        if (position==0)
            holder.isMe.visibility = View.VISIBLE
        else
            holder.isMe.visibility = View.GONE

        holder.name.text = flist?.get(position)?.name
        holder.email.text = flist?.get(position)?.memo
    }

    override fun getItemCount(): Int {
        return flist?.size ?: 0
    }

    fun refreshFriendList(newList : List<Friend>){
        flist = newList
    }

}