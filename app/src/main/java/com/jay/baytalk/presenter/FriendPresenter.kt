package com.jay.baytalk.presenter

import com.jay.baytalk.contract.FriendConstract
import com.jay.baytalk.model.FriendList

class FriendPresenter : FriendConstract.Presenter {
    private var searchView: FriendConstract.View? = null
    private val TAG = "로그"

    override fun buttonClickAction() {
        searchView?.showToast("Success")
    }

    override fun getFriendList() {
        FriendList.loadFriend { value ->
            value?:return@loadFriend
            searchView?.loadFriendList(value)
        }
    }

    override fun takeView(view: FriendConstract.View) {
        searchView = view
    }

    override fun dropView() {
        searchView = null
        FriendList.removeListener()
    }
}


