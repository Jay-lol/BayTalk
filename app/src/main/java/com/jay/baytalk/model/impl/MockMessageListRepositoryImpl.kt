package com.jay.baytalk.model.impl

import com.jay.baytalk.model.MessageListRepository
import com.jay.baytalk.model.data.MessageData

class MockMessageListRepositoryImpl : MessageListRepository {
    override fun getMessageList(roomId: String?, callback: (List<MessageData>?) -> Unit) {
        val messageList = mutableListOf<MessageData>()
        messageList.add(
            MessageData(
                "1234test",
                " 익명사용자",
                "안녕하세요",
                System.currentTimeMillis()
            )
        )
        callback(messageList)
    }

    override fun sendMessage(message: String, name: String?, userUid: List<String>?) {
        return updateLastMessage(message, userUid)
    }

    override fun updateLastMessage(message: String, userUid: List<String>?) {
        return
    }

    override fun disconnectServer() {
        return
    }
}