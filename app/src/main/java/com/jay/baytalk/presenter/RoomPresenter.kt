package com.jay.baytalk.presenter

import android.os.Message
import com.jay.baytalk.MyCallback
import com.jay.baytalk.model.MessageList

class RoomPresenter : RoomConstract.Presenter{
    private var searchView : RoomConstract.View? = null
    private val TAG = "RoomPresenter"

    override fun getMessage(roomId : String?, myCallback: MyCallback) {
        MessageList.getMessageList(roomId, object : MyCallback{
            override fun onCallback(value: List<Any>?) {
                myCallback.onCallback(value)
            }
        })
    }

    override fun sendMessage(message: String, name: String?, userUid: List<String>?) {
        MessageList.sendMessage(message, name, object : MyCallback{
            override fun onCallback(value: List<Any>?) {

            }
        })
        MessageList.updateLastMessage(message, userUid)
    }

    override fun takeView(view: RoomConstract.View) {
        searchView = view
    }

}