package com.jay.baytalk

interface OnItemClick {
    fun onChatRoomDelete(rid : String)
    fun onChatroomClick(rid : String, chatName : String, userUids: List<String>)
}