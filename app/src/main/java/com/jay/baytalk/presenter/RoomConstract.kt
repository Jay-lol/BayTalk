package com.jay.baytalk.presenter

import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView
import com.jay.baytalk.model.data.MessageData

interface RoomConstract {
    interface View : BaseView<Presenter>{
        fun showList(message : List<MessageData>)
    }

    interface Presenter : BasePresenter<View>{
        fun getMessage(roomId : String?)
        fun sendMessage(message:String, name:String?, userUid: List<String>?)
    }
}