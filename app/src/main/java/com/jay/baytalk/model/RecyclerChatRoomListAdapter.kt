package com.jay.baytalk.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.R
import kotlinx.android.synthetic.main.recycler_chatroom.view.*

class RecyclerChatRoomListAdapter(cList : List<ChatRoom>?, listner : OnItemClick) : RecyclerView.Adapter<RecyclerChatRoomListAdapter.cViewH>(){

    private var clist = cList
    private val callback = listner

    class cViewH(view: View) : RecyclerView.ViewHolder(view) {
        var rid : String? = null
        var chatName = view.roomName
        var lastMessage = view.roomMemo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cViewH {
        return cViewH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_chatroom, parent, false)
        )
    }

    override fun onBindViewHolder(holder: cViewH, position: Int) {
        holder.chatName.text = clist?.get(position)?.usersName
        holder.lastMessage.text = clist?.get(position)?.lastMessage
        holder.rid = clist?.get(position)?.roomId as String
        setButton(holder ,holder.rid , holder.chatName.text.toString(), clist?.get(position)?.userUid)
    }

    private fun setButton(
        holder: cViewH,
        rid: String?,
        chatName: String,
        userUid: List<String>?
    ) {
        holder.itemView.setOnLongClickListener(object  : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                callback.onClick(rid!!, 1, 1)
                return true
            }
        })

        holder.itemView.setOnClickListener {
            userUid?.let { it1 -> callback.onClick2(rid!!, chatName, it1) }
        }


    }


    override fun getItemCount(): Int {
        return clist?.size ?: 0
    }
    fun refresh(newList : List<ChatRoom>){
        clist = newList
    }
}