package com.jay.baytalk.model

import android.annotation.SuppressLint
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.model.data.MessageData

object MessageList {
    private val TAG = "MessageList"
    private var rid: String? = null

    fun getMessageList(roomId: String?, callback: (List<MessageData>?) -> Unit) {
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
                            x.child("userId").value as String,
                            x.child("name").value as String,
                            x.child("message").value as String,
                            x.child("time").value as Long
                        )
                    )
                }
                callback(messageList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String, name: String?) {
        rid ?: return
        val database = Firebase.database
        val myRef = database.getReference("Message/$rid")
        myRef.push().setValue(
            hashMapOf(
                Pair("name", name),
                Pair("message", message),
                Pair("userId", Firebase.auth.currentUser?.uid),
                Pair("time", System.currentTimeMillis())
            )
        )
    }

    fun updateLastMessage(message: String, userUid: List<String>?) {
        val database = Firebase.database
        var myRef: DatabaseReference
        if (userUid != null) {
            for (x in userUid) {
                myRef = database.getReference("RoomUser/$x/$rid")
                myRef.updateChildren(mapOf(Pair("lastMessage", message)))
            }
        }

    }
}