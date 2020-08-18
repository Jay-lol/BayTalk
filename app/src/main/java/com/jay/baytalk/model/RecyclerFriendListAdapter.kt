package com.jay.baytalk.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.R
import kotlinx.android.synthetic.main.recycler_friend.view.*


class RecyclerFriendListAdapter(fList : List<Friend>?) : RecyclerView.Adapter<RecyclerFriendListAdapter.fViewH>() {

    private var flist = fList

    class fViewH(view: View) : RecyclerView.ViewHolder(view) {
        var rid : Int? = null
        var name = view.name
        var email = view.email
        var isMe = view.itsme
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fViewH {
        return fViewH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_friend, parent, false))
    }

    override fun onBindViewHolder(holder: fViewH, position: Int) {
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

    fun refresh(newList : List<Friend>){
        flist = newList
    }

}