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

class MessageList {
    private val TAG = "로그 ${javaClass.simpleName}"
    private var rid: String? = null
    private var job : ValueEventListener? = null
    private val database by lazy { Firebase.database }
    private lateinit var messageServerReference: DatabaseReference

    /**
     * 키값 상수화
     */
    private val USER_UID = "userUid"
    private val NAME = "name"
    private val LAST_MESSAGE = "lastMessage"
    private val MESSAGE = "message"
    private val TIME = "time"


    fun getMessageList(roomId: String?, callback: (List<MessageData>?) -> Unit) {
        rid = roomId

        messageServerReference = database.getReference("Message/$roomId")

        job = messageServerReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "MessageList ~ onDataChange() called")
                val value = snapshot.children
                val messageList = mutableListOf<MessageData>()
                for (x in value) {
                    messageList.add(
                        MessageData(
                            x.child(USER_UID).value as String,
                            x.child(NAME).value as String,
                            x.child(MESSAGE).value as String,
                            x.child(TIME).value as Long
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
    fun sendMessage(message: String, name: String?, userUid: List<String>?) {
        rid ?: return
        sendRef = database.getReference("Message/$rid")
        sendRef.push().setValue(
            hashMapOf(
                Pair(NAME, name),
                Pair(MESSAGE, message),
                Pair(USER_UID, Firebase.auth.currentUser?.uid),
                Pair(TIME,  System.currentTimeMillis())
            )
        )
        updateLastMessage(message, userUid)
    }

    lateinit var updateLastMRef : DatabaseReference
    private fun updateLastMessage(message: String, userUid: List<String>?) {
        if (userUid != null) {
            for (x in userUid) {
                updateLastMRef = database.getReference("RoomUser/$x/$rid")
                updateLastMRef.updateChildren(mapOf(Pair(LAST_MESSAGE, message)))
            }
        }
    }

    fun disconnectServer() {
        job?:return
        messageServerReference.removeEventListener(job!!)
    }
}