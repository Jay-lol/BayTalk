package com.jay.baytalk.model

import com.jay.baytalk.model.data.ChatRoom

interface ChatRoomListRepository {
    fun getChatRoomList(callback: (MutableList<ChatRoom>) -> Unit)

    fun deleteChatRoomList(rid: String, callback: (Boolean) -> Unit)

    fun removeListener()

    fun sendFcmId(fcm: String)

    fun setFcm(callback: (Boolean) -> Unit)

    fun showError(error: String)

    fun makeChatroom(list: List<List<String>>, callback: (Boolean) -> Unit)
}