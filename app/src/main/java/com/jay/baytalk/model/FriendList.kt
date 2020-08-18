package com.jay.baytalk.model

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.MyCallback
import java.util.*

object FriendList {
    private val TAG = "FriendList"
    private var friendList = mutableListOf<Friend>()
    fun getFriendlist(myCallback: MyCallback) {
        val database = Firebase.database
        val myRef = database.getReference("Users")
        val myUid = Firebase.auth.currentUser?.uid
        // Read from the database
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                friendList.clear()
                for (x in value) {
                    if (myUid == x.key) {
                        friendList.add(0,
                            Friend(
                                x.key, x.child("name").value as String
                                , x.child("statusMessage").value as String
                                , false
                            )
                        )
                    } else {
                        friendList.add(
                            Friend(
                                x.key, x.child("name").value as String
                                , x.child("statusMessage").value as String
                                , false
                            )
                        )
                        Log.d(TAG, "" + x.key) //displays the key for the node
                        Log.d(TAG, "" + x.child("statusMessage").value)   //gives the value for given keyname
                    }
                }

//                friendList = friendList.sortedWith(Comparator { a, b ->
//                        when {
//                            a.name > b.name -> 1
//                            a.name < b.name -> -1
//                            else -> 0
//                        }
//                }).toMutableList()

                myCallback.onCallback(friendList)
            }

            override fun onCancelled(error: DatabaseError) {
                showError("DataLoading Error")
            }
        })


    }


    fun getFriendlistData(): List<Friend> {
        return friendList
    }

    fun showError(error: String) {
        Log.d(TAG, error)
    }
}

