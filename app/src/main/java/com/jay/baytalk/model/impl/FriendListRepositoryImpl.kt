package com.jay.baytalk.model.impl

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.model.FriendListRepository
import com.jay.baytalk.model.data.Friend

class FriendListRepositoryImpl : FriendListRepository{
    private val TAG = "로그 ${javaClass.simpleName}"
    private lateinit var friendList: MutableList<Friend>
    private val database = Firebase.database
    private var job: ValueEventListener? = null
    val myUid = Firebase.auth.currentUser?.uid

    /**
     * 키값 상수화
     */
    private val NAME = "name"
    private val STATUS_MESSAGE = "statusMessage"

    override fun loadFriend(callback: (List<Friend>?) -> Unit) {
        // Read from the database
        val loadFriendRef = database.getReference("Users")
        loadFriendRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                friendList = mutableListOf()
                var myData: Friend? = null
                for (x in value) {
                    if (myUid == x.key) {
                        myData = Friend(
                            x.key,
                            x.child(NAME).value as String,
                            x.child(STATUS_MESSAGE).value as String,
                            false
                        )
                    } else {
                        friendList.add(
                            Friend(
                                x.key,
                                x.child(NAME).value as String,
                                x.child(STATUS_MESSAGE).value as String,
                                false
                            )
                        )
                        //displays the key for the node
                        //gives the value for given keyname
                        Log.d(TAG, "${x.key} ${x.child(STATUS_MESSAGE).value}")
                    }
                }
                myData ?: return
                friendList = friendList.sortedWith(compareBy { it.name }).toMutableList()
                friendList.add(0, myData)
                callback(friendList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })


    }

    private lateinit var myInfoRef : DatabaseReference
    override fun loadMyInfo(currentUser: FirebaseUser, callback: (String) -> Unit) {
        myInfoRef = database.getReference("Users/${currentUser.uid}/name")
        // Read from the database

        //addValueEventListener를 활용하여 채팅방을 구현해야한다.  onDataChange는 데이터가 변경될 때마다 호출되기 때문
        job = myInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                value ?: return
                callback(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun removeListener() {
        job?.let { listener ->
            myInfoRef.removeEventListener(listener)
        }
    }
}