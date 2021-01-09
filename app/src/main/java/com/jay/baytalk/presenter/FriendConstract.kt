package com.jay.baytalk.presenter

import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView
import com.jay.baytalk.model.data.Friend

interface FriendConstract {

    interface View : BaseView<Presenter> {
        fun showList(list : List<Any>)
        fun loadFriendList(value : List<Friend>)
    }

    interface Presenter : BasePresenter<View> {
        fun getFriendList()
        fun buttonClickAction()
    }

}