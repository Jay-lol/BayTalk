package com.jay.baytalk.model

data class ChatRoom(
    val roomId : String,
    val usersName : String,
    val roomType : String,
    val lastMessage : String,
    val time : Long,
    val userUid : List<String>
)
