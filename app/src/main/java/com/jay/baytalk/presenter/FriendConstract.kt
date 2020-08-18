package com.jay.baytalk.presenter

import com.jay.baytalk.MyCallback
import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView

interface FriendConstract {

    interface View : BaseView<Presenter> {
        fun showList(list : List<Any>)
    }

    interface Presenter : BasePresenter<View> {
        fun getFriendList(myCallback: MyCallback)
        fun buttonClickAction()
    }

}