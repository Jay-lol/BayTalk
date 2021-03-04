package com.jay.baytalk.model

import com.google.firebase.auth.FirebaseUser
import com.jay.baytalk.model.data.Friend

interface FriendListRepository {
    fun loadFriend(callback: (List<Friend>?) -> Unit)

    fun loadMyInfo(currentUser: FirebaseUser, callback: (String) -> Unit)

    fun removeListener()
}