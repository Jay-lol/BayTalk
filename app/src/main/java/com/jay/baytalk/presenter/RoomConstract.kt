package com.jay.baytalk.presenter

import com.jay.baytalk.MyCallback
import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView

interface RoomConstract {
    interface View : BaseView<Presenter>{
        fun showList(message : ArrayList<List<String>>)
    }

    interface Presenter : BasePresenter<View>{
        fun getMessage(roomId : String?, myCallback: MyCallback)
        fun sendMessage(message:String, name:String?, userUid: List<String>?)
    }
}