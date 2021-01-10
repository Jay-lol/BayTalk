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
import com.jay.baytalk.model.data.MessageData

object MessageList {
    private val TAG = "로그 ${javaClass.simpleName}"
    private var rid: String? = null
    private var job : ValueEventListener? = null
    private val database by lazy { Firebase.database }
    private lateinit var myRef : DatabaseReference
    fun getMessageList(roomId: String?, callback: (List<MessageData>?) -> Unit) {
        rid = roomId

        myRef = database.getReference("Message/$roomId")

        job = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "MessageList ~ onDataChange() called")
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

    lateinit var sendRef : DatabaseReference
    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String, name: String?) {
        rid ?: return
        sendRef = database.getReference("Message/$rid")
        sendRef.push().setValue(
            hashMapOf(
                Pair("name", name),
                Pair("message", message),
                Pair("userId", Firebase.auth.currentUser?.uid),
                Pair("time", System.currentTimeMillis())
            )
        )
    }

    lateinit var updateLastMRef : DatabaseReference
    fun updateLastMessage(message: String, userUid: List<String>?) {
        if (userUid != null) {
            for (x in userUid) {
                updateLastMRef = database.getReference("RoomUser/$x/$rid")
                updateLastMRef.updateChildren(mapOf(Pair("lastMessage", message)))
            }
        }
    }

    fun removeListener() {
        job?:return
        myRef.removeEventListener(job!!)
    }
}