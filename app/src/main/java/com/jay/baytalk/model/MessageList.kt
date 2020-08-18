package com.jay.baytalk.model

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.MyCallback
import java.util.*

object MessageList {
    private val TAG = "MessageList"
    private var rid : String? = null

    fun getMessageList(roomId : String?, myCallback: MyCallback) {
        rid = roomId
        val database = Firebase.database
        val myRef = database.getReference("Message/$roomId")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                val messageList = mutableListOf<MessageData>()
                for (x in value) {
                    messageList.add(
                        MessageData(
                            x.child("userId").value as String
                            , x.child("name").value as String
                            , x.child("message").value as String
                            , x.child("time").value as Long
                        )
                    )
                }
                myCallback.onCallback(messageList)
            }

            override fun onCancelled(error: DatabaseError) {
                ChatRoomList.showError("DataLoading Error")
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String, name: String?, myCallback: MyCallback) {
        if (rid != null) {
            val database = Firebase.database
            val myRef = database.getReference("Message/$rid")
            myRef.push().setValue(hashMapOf(Pair("name", name)
                , Pair("message", message)
                , Pair("userId", Firebase.auth.currentUser?.uid)
                , Pair("time", System.currentTimeMillis())
            ))
            myCallback.onCallback(listOf())
        } else{
            Log.d(TAG, "rid is null")
        }
    }

    fun updateLastMessage(message: String, userUid: List<String>?) {
        val database = Firebase.database
        var myRef : DatabaseReference
        if (userUid != null) {
            for (x in userUid){
                myRef = database.getReference("RoomUser/$x/$rid")
                myRef.updateChildren(mapOf(Pair("lastMessage", message)))
            }
        }

    }
}