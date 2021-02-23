package com.jay.baytalk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.FriendList
import com.jay.baytalk.model.data.ChatRoom
import com.jay.baytalk.model.data.Friend

class FriendAndRoomListViewModel : ViewModel() {

    private val friendListRepository by lazy {
        FriendList()
    }

    private val roomListRepository by lazy {
        ChatRoomList()
    }

    private val _friendListLiveData : MutableLiveData<List<Friend>> = MutableLiveData()
    val friendListLiveData : LiveData<List<Friend>> get() = _friendListLiveData

    private val _chatRoomListLiveData : MutableLiveData<List<ChatRoom>> = MutableLiveData()
    val chatRoomListLiveData : LiveData<List<ChatRoom>> get() = _chatRoomListLiveData

    fun getFriendList() {
        friendListRepository.loadFriend{ list ->
            _friendListLiveData.postValue(list)
        }
    }

    fun getChatList() {
        roomListRepository.getChatRoomList { chatRoomList ->
            _chatRoomListLiveData.postValue( chatRoomList )
        }
    }

    fun deleteChatRoom(roomId: String) {
        roomListRepository.deleteChatRoomList(roomId){
            // callback or LiveData
        }
    }

    override fun onCleared() {
        friendListRepository.removeListener()
        roomListRepository.removeListener()
        super.onCleared()
    }


}