package com.jay.baytalk.model

import com.jay.baytalk.model.data.MessageData

interface MessageListRepository {
    fun getMessageList(roomId: String?, callback: (List<MessageData>?) -> Unit)

    fun sendMessage(message: String, name: String?, userUid: List<String>?)

    fun updateLastMessage(message: String, userUid: List<String>?)

    fun disconnectServer()
}
