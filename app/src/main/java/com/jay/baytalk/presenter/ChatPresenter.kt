package com.jay.baytalk.presenter

import android.util.Log
import com.jay.baytalk.MyCallback
import com.jay.baytalk.model.ChatRoom
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.FriendList

class ChatPresenter(view: ChatConstract.View) : ChatConstract.Presenter {
    private val searchView: ChatConstract.View = view
    private val TAG = "FriendPresenter"

    init {
        searchView.setPresenter(this)
    }

    override fun buttonClickAction() {
        searchView.ShowToast("Success")
    }

    override fun deleteChatRoom(rid: String) {
        ChatRoomList.deleteChatRoomList(object : MyCallback{
            override fun onCallback(value: List<Any>?) {
                if (value != null) {
                    Log.d(TAG, "채팅방 삭제 완료!")
                }
            }
        }, rid)
    }
    
    override fun getChatList(myCallback: MyCallback) {
        ChatRoomList.getChatRoomList(object : MyCallback{
            override fun onCallback(value: List<Any>?) {
                if (value != null) {
                    myCallback.onCallback(value)
                } else
                    Log.d(TAG , "No Friend")
            }
        }, 0)

    }
}