package com.jay.baytalk.model

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonParser
import com.jay.baytalk.MyCallback
import com.jay.baytalk.base.BaseView

object ChatRoomList {
    private val TAG = "ChatRoomList"

    fun getChatRoomList(myCallback: MyCallback, roomNum: Int) {

        val database = Firebase.database
        var myRef = database.getReference("RoomUser/${Firebase.auth.currentUser?.uid}")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                val chatRoomList = mutableListOf<ChatRoom>()
                for (x in value) {
                    chatRoomList.add(
                        ChatRoom(
                            x.child("roomId").value as String
                            , x.child("userName").value as String
                            , x.child("roomType").value as String
                            , x.child("lastMessage").value as String
                            , x.child("time").value as Long
                            , x.child("userUid").value as List<String>
                        )
                    )
                }
                myCallback.onCallback(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {
                showError("DataLoading Error")
            }
        })

    }

    fun deleteChatRoomList(myCallback: MyCallback, rid : String){
        val database = Firebase.database
        var myRef = database.getReference("RoomUser/${Firebase.auth.currentUser?.uid}/$rid")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myRef.removeValue()
                myCallback.onCallback(listOf("clear"))
            }

            override fun onCancelled(error: DatabaseError) {
                showError("DataLoading Error")
            }
        })
    }

    fun showError(error: String) {
        Log.d(TAG, error)
    }

}