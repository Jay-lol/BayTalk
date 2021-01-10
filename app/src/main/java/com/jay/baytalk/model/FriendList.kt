package com.jay.baytalk.model

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.model.data.Friend

object FriendList {
    private val TAG = "로그 ${javaClass.simpleName}"
    private lateinit var friendList : MutableList<Friend>
    private val database = Firebase.database
    private var myRef = database.getReference("Users")
    private var job : ValueEventListener? = null
    val myUid = Firebase.auth.currentUser?.uid

    fun loadFriend(callback : (List<Friend>?) -> Unit) {
        // Read from the database
        val myLoadRef = database.getReference("Users")
        myLoadRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                friendList = mutableListOf()
                var myData : Friend? = null
                for (x in value) {
                    if (myUid == x.key) {
                        myData = Friend(
                                x.key, x.child("name").value as String
                                , x.child("statusMessage").value as String
                                , false
                            )
                    } else {
                        friendList.add(
                            Friend(
                                x.key, x.child("name").value as String
                                , x.child("statusMessage").value as String
                                , false
                            )
                        )
                        //displays the key for the node
                        //gives the value for given keyname
                        Log.d(TAG, "${x.key} ${x.child("statusMessage").value}")
                    }
                }
                myData?:return
                friendList = friendList.sortedWith(compareBy { it.name }).toMutableList()
                friendList.add(0, myData)
                callback(friendList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })


    }

    fun loadMyInfo(currentUser: FirebaseUser, callback: (String) -> Unit) {
        myRef = database.getReference("Users/${currentUser.uid}/name")
        // Read from the database

        //addValueEventListener를 활용하여 채팅방을 구현해야한다.  onDataChange는 데이터가 변경될 때마다 호출되기 때문
        job = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                value?:return
                callback(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun removeListener(){
        job?:return
        myRef.removeEventListener(job!!)
    }
}

