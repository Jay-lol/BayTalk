package com.jay.baytalk.presenter

import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView
import com.jay.baytalk.model.data.ChatRoom

interface RoomListConstract {

    interface View : BaseView<Presenter> {
        fun showList(list : MutableList<ChatRoom>)
    }

    interface Presenter : BasePresenter<View> {
        fun getChatList()
        fun buttonClickAction()
        fun deleteChatRoom(rid: String)
    }
}