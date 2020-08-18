package com.jay.baytalk.presenter

import com.jay.baytalk.MyCallback
import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView

interface ChatConstract {

    interface View : BaseView<Presenter> {
        fun showList(list : List<Any>)
    }

    interface Presenter : BasePresenter<View> {
        fun getChatList(myCallback: MyCallback)
        fun buttonClickAction()
        fun deleteChatRoom(rid: String)
    }
}