package com.jay.baytalk.presenter

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.jay.baytalk.contract.MainConstract
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.FriendList

class MainPresenter : MainConstract.Presenter {
    private var searchView : MainConstract.View? = null
    private val TAG: String = "로그 ${this.javaClass.simpleName}"

    override fun welcome(currentUser: FirebaseUser) {
        FriendList.loadMyInfo(currentUser){ name ->
            searchView?.welcomeMent(name)
        }
    }

    override fun takeView(view: MainConstract.View) {
        searchView = view
    }

    override fun dropView() {
        searchView = null
    }

    override fun setFcm(context: Context) {
        ChatRoomList.setFcm(context){ result ->
            if (result) Log.d(TAG, "Fcm등록 성공")
            else Log.d(TAG, "Fcm등록 실패")
        }
    }


}
