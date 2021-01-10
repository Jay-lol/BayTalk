package com.jay.baytalk.presenter

import com.jay.baytalk.contract.RoomListConstract
import com.jay.baytalk.model.ChatRoomList

class RoomListPresenter : RoomListConstract.Presenter {
    private var searchView: RoomListConstract.View? = null
    private val TAG = "FriendPresenter"

    override fun buttonClickAction() {
        searchView?.showToast("Success")
    }

    override fun deleteChatRoom(rid: String) {
        ChatRoomList.deleteChatRoomList(rid){ success ->
            if (success) searchView?.showToast("Success")
            else searchView?.showToast("Fail")
        }
    }

    override fun takeView(view: RoomListConstract.View) {
        searchView = view
    }

    override fun dropView() {
        searchView = null
        ChatRoomList.removeListener()
    }

    override fun getChatList() {
        ChatRoomList.getChatRoomList{ value ->
            searchView?.showList(value)
        }
    }
}