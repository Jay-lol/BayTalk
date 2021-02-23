package com.jay.baytalk.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.R
import com.jay.baytalk.model.data.ChatRoom

class ChatRoomList {
    private val TAG = "로그 ${this.javaClass.simpleName}"
    private val database = Firebase.database
    private lateinit var myRef: DatabaseReference
    private var job : ValueEventListener? = null

    /**
     * 키값 상수화
     */
    private val ROOM_ID = "roomId"
    private val USER_UID = "userUid"
    private val USER_NAME = "userName"
    private val ROOM_TYPE = "roomType"
    private val LASTMESSAGE = "lastMessage"
    private val LAST_CHAT_TIME = "time"

    fun getChatRoomList(callback: (MutableList<ChatRoom>) -> Unit) {

        myRef = database.getReference("RoomUser/${Firebase.auth.currentUser?.uid}")

        job = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                val chatRoomList = mutableListOf<ChatRoom>()
                for (x in value) {
                    chatRoomList.add(
                        ChatRoom(
                            x.child(ROOM_ID).value as String,
                            x.child(USER_NAME).value as String,
                            x.child(ROOM_TYPE).value as String,
                            x.child(LASTMESSAGE).value as String,
                            x.child(LAST_CHAT_TIME).value as Long,
                            x.child(USER_UID).value as List<String>
                        )
                    )
                }
                callback(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {
                showError("DataLoading Error")
            }
        })

    }

    fun deleteChatRoomList(rid: String, callback: (Boolean) -> Unit) {
        myRef = database.getReference("RoomUser/${Firebase.auth.currentUser?.uid}/$rid")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val job = myRef.removeValue()
                job.addOnCompleteListener {
                    if (it.isComplete)
                        callback(true)
                }
                job.addOnFailureListener {
                    callback(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showError("DataLoading Error")
                callback(false)
            }
        })
    }

    fun removeListener() {
        job?:return
        myRef.removeEventListener(job!!)
    }

    fun sendFcmId(fcm: String) {
        myRef = database.getReference("FcmId/${Firebase.auth.currentUser?.uid}")
        myRef.setValue(fcm)
    }

    fun setFcm(context : Context, callback : (Boolean) -> Unit){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    callback(false)
                    return@OnCompleteListener
                }
                task.result?:return@OnCompleteListener
                // Get new Instance ID token
                val token = task.result!!.token
                // Log and toast
                val msg = context.getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                sendFcmId(token)
                callback(true)
            })
    }

    fun showError(error: String) {
        Log.d(TAG, error)
    }

    fun makeChatroom(list: List<List<String>>, callback: (Boolean) -> Unit) {
        val size = list.size
        val nTime = System.currentTimeMillis()
        val roomName = "${list[0][0]}@$nTime@${size}방"
        // 방장아이디@방만든시간@방인원
        val nameList = mutableListOf<String>()
        var userNameList = ""
        for (i in list.indices) {
            if (i != size - 1)
                userNameList += list[i][1] + ","
            else
                userNameList += list[i][1]
            nameList.add(list[i][0])
        }

        Thread {
            try {
                for (i in list) {
                    myRef = database.getReference("RoomUser/${i[0]}/$roomName")
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[LASTMESSAGE] = " "
                    hashMap[ROOM_ID] = roomName
                    if (size > 2) hashMap[ROOM_TYPE] = "Group" else hashMap[ROOM_TYPE] = "Private"
                    hashMap[LAST_CHAT_TIME] = nTime
                    hashMap[USER_NAME] = userNameList
                    hashMap[USER_UID] = nameList
                    myRef.setValue(hashMap)
                }

                for (name in nameList) {
                    myRef = database.getReference("UserInRoom/$roomName/$name")
                    myRef.setValue(true)
                }
                Handler(Looper.getMainLooper()).post{
                    callback(true)
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post{
                    callback(false)
                }
            }
        }.start()
    }
}