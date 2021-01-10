package com.jay.baytalk.contract

import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView
import com.jay.baytalk.model.data.Friend

interface MakeChatConstract {
    // 뷰가 가지는 함수, BaseView를 통해 toast같은 기본적인 함수도 가능
    interface View : BaseView<Presenter> {
        fun setAdapter(fList: List<Friend>)
        fun closeView()
    }

    // 프레젠터가 가지는 함수, BasePresenter를 통해 뷰를 가진다.
    interface Presenter : BasePresenter<View> {
        fun inviteFriend(list : List<List<String>>)
        fun getFriendList()
    }
}