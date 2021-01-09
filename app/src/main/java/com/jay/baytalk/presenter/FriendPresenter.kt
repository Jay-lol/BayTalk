package com.jay.baytalk.presenter

import android.util.Log
import com.jay.baytalk.model.data.Friend
import com.jay.baytalk.model.FriendList

class FriendPresenter(view: FriendConstract.View) : FriendConstract.Presenter {
    private val searchView: FriendConstract.View = view
    private val TAG = "로그"

    init {
        searchView.setPresenter(this)
    }

    override fun buttonClickAction() {
        searchView.showToast("Success")
    }

    override fun getFriendList() {
        FriendList.loadFriend { value ->
            value?:return@loadFriend
            searchView.loadFriendList(value)
        }
    }
}


