package com.jay.baytalk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jay.baytalk.model.MessageList
import com.jay.baytalk.model.data.MessageData

class MessageRoomViewModel : ViewModel() {

    private val  TAG : String = "로그 ${this.javaClass.simpleName}"

    private val messageListRepository by lazy {
        MessageList()
    }

    private val _messageLiveData : MutableLiveData<List<MessageData>> = MutableLiveData()
    val messageLiveData : LiveData<List<MessageData>> get() = _messageLiveData

    fun connectServer(roomId : String?){
        messageListRepository.getMessageList(roomId){ messageData ->
            if (messageData.isNullOrEmpty()){
                Log.d(TAG, "Message is Empty!")
            }
            _messageLiveData.value = messageData
        }
    }

    fun sendMessage(message: String, userName: String?, userUid: List<String>?) {
        messageListRepository.sendMessage(message, userName, userUid)
    }

    override fun onCleared() {
        messageListRepository.disconnectServer()
        super.onCleared()
    }

}