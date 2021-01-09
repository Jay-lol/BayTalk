package com.jay.baytalk.presenter

import com.jay.baytalk.model.ChatRoomList

class RoomListPresenter(view: RoomListConstract.View) : RoomListConstract.Presenter {
    private val searchView: RoomListConstract.View = view
    private val TAG = "FriendPresenter"

    init {
        searchView.setPresenter(this)
    }
    
    override fun buttonClickAction() {
        searchView.showToast("Success")
    }

    override fun deleteChatRoom(rid: String) {
        ChatRoomList.deleteChatRoomList(rid){ success ->
            if (success) searchView.showToast("Success")
            else searchView.showToast("Fail")
        }
    }
    
    override fun getChatList() {
        ChatRoomList.getChatRoomList{ value ->
            searchView.showList(value)
        }
    }
}