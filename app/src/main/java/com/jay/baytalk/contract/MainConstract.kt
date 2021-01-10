package com.jay.baytalk.contract

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.jay.baytalk.base.BasePresenter
import com.jay.baytalk.base.BaseView

interface MainConstract {

    interface View : BaseView<Presenter>{
        fun showLoading()
        fun welcomeMent(name : String)
    }

    interface Presenter : BasePresenter<View>{
        fun welcome(currentUser: FirebaseUser)
        fun setFcm(context: Context)
    }
}