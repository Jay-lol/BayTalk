package com.jay.baytalk.presenter

import android.util.Log
import com.jay.baytalk.model.MessageList

class RoomPresenter : RoomConstract.Presenter {
    private var searchView : RoomConstract.View? = null
    private val TAG = "로그"

    override fun getMessage(roomId : String?) {
        MessageList.getMessageList(roomId){ value ->
            if (value.isNullOrEmpty()){
                Log.d(TAG, "Message is Empty!")
                return@getMessageList
            }
            searchView?.showList(value)
        }
    }

    override fun sendMessage(message: String, name: String?, userUid: List<String>?) {
        MessageList.sendMessage(message, name)
        MessageList.updateLastMessage(message, userUid)
    }

    override fun takeView(view: RoomConstract.View) {
        searchView = view
    }

}