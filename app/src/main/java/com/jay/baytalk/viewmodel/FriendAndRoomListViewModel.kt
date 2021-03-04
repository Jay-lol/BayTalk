package com.jay.baytalk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.jay.baytalk.model.ChatRoomListRepository
import com.jay.baytalk.model.FriendListRepository
import com.jay.baytalk.model.impl.ChatRoomListRepositoryImpl
import com.jay.baytalk.model.impl.FriendListRepositoryImpl
import com.jay.baytalk.model.data.ChatRoom
import com.jay.baytalk.model.data.Friend

class FriendAndRoomListViewModel : ViewModel() {
    private val  TAG : String = "로그 ${this.javaClass.simpleName}"

    private val friendListRepository : FriendListRepository by lazy {
        FriendListRepositoryImpl()
    }

    private val roomListRepository : ChatRoomListRepository by lazy {
        ChatRoomListRepositoryImpl()
    }

    private val _friendListLiveData : MutableLiveData<List<Friend>> = MutableLiveData()
    val friendListLiveData : LiveData<List<Friend>> get() = _friendListLiveData

    private val _chatRoomListLiveData : MutableLiveData<List<ChatRoom>> = MutableLiveData()
    val chatRoomListLiveData : LiveData<List<ChatRoom>> get() = _chatRoomListLiveData

    private val _inviteSuccessMessage : MutableLiveData<String> = MutableLiveData()
    val inviteSuccessMessage : LiveData<String> get() = _inviteSuccessMessage

    private val _myNameData : MutableLiveData<String> = MutableLiveData()
    val myNameData : LiveData<String> get() = _myNameData

    fun getFriendList() {
        friendListRepository.loadFriend{ list ->
            _friendListLiveData.postValue(list)
        }
    }

    fun getChatList() {
        roomListRepository.getChatRoomList { chatRoomList ->
            _chatRoomListLiveData.postValue( chatRoomList.sortedByDescending {  it.time } )
        }
    }

    fun deleteChatRoom(roomId: String) {
        roomListRepository.deleteChatRoomList(roomId){
            // callback or LiveData
        }
    }

    fun inviteFriend(list: List<List<String>>) {
        val size = list.size
        roomListRepository.makeChatroom(list){ success ->
            if(!success)
                _inviteSuccessMessage.value = "서버 에러로 초대 실패!"
             else
                _inviteSuccessMessage.value = "${size - 1} 명을 초대했습니다"
            _inviteSuccessMessage.value = "INIT"
        }
    }

    fun loadMyName(currentUser: FirebaseUser) {
        friendListRepository.loadMyInfo(currentUser){ name ->
            _myNameData.value = name
        }
    }

    fun setFcm() {
        roomListRepository.setFcm{ result ->
            if (result) Log.d(TAG, "Fcm등록 성공")
            else Log.d(TAG, "Fcm등록 실패")
        }
    }

    override fun onCleared() {
        friendListRepository.removeListener()
        roomListRepository.removeListener()
        super.onCleared()
    }

}