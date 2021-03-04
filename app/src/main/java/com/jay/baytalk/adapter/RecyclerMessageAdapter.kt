package com.jay.baytalk.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.R
import com.jay.baytalk.adapter.diffutil.MessageDiffUtil
import com.jay.baytalk.databinding.RecyclerFriendMessageBinding
import com.jay.baytalk.databinding.RecyclerMyMessageBinding
import com.jay.baytalk.model.data.MessageData
import java.text.SimpleDateFormat

class RecyclerMessageAdapter(
    uid: String?
) : ListAdapter<MessageData, RecyclerMessageAdapter.MessageViewHolder>(MessageDiffUtil) {

    private val uId = uid

    inner class MessageViewHolder(val view: ViewDataBinding) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SimpleDateFormat")
        fun onBind(message: MessageData, position: Int) {
            val timeFormat = SimpleDateFormat("a K:mm").format(message.time)
            when(view){

                // freind Message
                is RecyclerFriendMessageBinding ->{
                    val layoutParams = view.fmessage.layoutParams as ConstraintLayout.LayoutParams

                    if (position >= 1
                        && message.userId == currentList[position - 1]?.userId
                        && timeFormat == SimpleDateFormat("a K:mm").format(currentList[position - 1]?.time)
                    ) {

                        view.fmessage.setBackgroundResource(R.drawable.theme_chatroom_bubble_you_02_image)
                        view.fName.visibility = View.GONE
                        layoutParams.topMargin = 0
                    } else {
                        view.fmessage.setBackgroundResource(R.drawable.theme_chatroom_bubble_you_01_image)
                        view.fName.visibility = View.VISIBLE
                        layoutParams.topMargin = 8
                    }

                    if (position < currentList.size - 1 &&
                        message.userId == currentList[position + 1]?.userId &&
                        timeFormat == SimpleDateFormat("a K:mm").format(currentList[position + 1]?.time)
                    ) {
                        view.ftime.visibility = View.GONE
                    } else {
                        view.ftime.visibility = View.VISIBLE
                    }

                    view.fmessage.layoutParams = layoutParams
                    view.fmessage.setTextColor(Color.BLACK)
                    view.fName.setTextColor(Color.BLACK)
                    view.fName.text = message.name
                    view.fmessage.text = message.message
                    view.ftime.text = timeFormat
                }

                // my Message
                is RecyclerMyMessageBinding -> {
                    val layoutParams = view.message.layoutParams as ConstraintLayout.LayoutParams

                    if (position >= 1
                        && message.userId == currentList[position - 1]?.userId
                        && (timeFormat == SimpleDateFormat("a K:mm").format(currentList[position - 1]?.time))
                    ) {
                        view.message.setBackgroundResource(R.drawable.theme_chatroom_bubble_me_02_image)
                        layoutParams.topMargin = 0
                    } else {
                        view.message.setBackgroundResource(R.drawable.theme_chatroom_bubble_me_01_image)
                        layoutParams.topMargin = 8
                    }

                    if (position < currentList.size - 1 &&
                        uId == currentList[position + 1]?.userId &&
                        timeFormat == SimpleDateFormat("a K:mm").format(currentList[position + 1]?.time)
                    ) {
                        view.time.visibility = View.GONE
                    } else {
                        view.time.visibility = View.VISIBLE
                    }

                    view.message.layoutParams = layoutParams
                    view.message.setTextColor(Color.BLACK)
                    view.message.text = message.message
                    view.time.text = timeFormat
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            0 -> MessageViewHolder(
                RecyclerFriendMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> MessageViewHolder(
                RecyclerMyMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).convertViewType()
    }

    @SuppressLint("SimpleDateFormat")
    private fun MessageData.convertViewType(): Int {
        return if (userId != uId) 0
        else 1
    }
}