package com.jay.baytalk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jay.baytalk.model.FriendList
import com.jay.baytalk.model.data.Friend

class FriendAndRoomListViewModel : ViewModel() {

    private val friendListRepository by lazy {
        FriendList()
    }

    private val _friendListLiveData : MutableLiveData<List<Friend>> = MutableLiveData()
    val friendListLiveData : LiveData<List<Friend>> get() = _friendListLiveData

    fun getFriendList() {
        friendListRepository.loadFriend{ list ->
            _friendListLiveData.postValue(list)
        }
    }

    override fun onCleared() {
        friendListRepository.removeListener()
        super.onCleared()
    }
}