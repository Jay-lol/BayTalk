package com.jay.baytalk.presenter

import android.util.Log
import com.jay.baytalk.MyCallback
import com.jay.baytalk.model.FriendList

class FriendPresenter(view: FriendConstract.View) : FriendConstract.Presenter {
    private val searchView: FriendConstract.View = view
    private val TAG = "FriendPresenter"

    init {
        searchView.setPresenter(this)
    }

    override fun buttonClickAction() {
        searchView.ShowToast("Success")
    }

    override fun getFriendList(myCallback: MyCallback) {
        FriendList.getFriendlist(object : MyCallback {
            override fun onCallback(value: List<Any>?) {
                Log.d(TAG, value.toString())
                if (value != null) {
                    myCallback.onCallback(value)
                } else
                    Log.d(TAG , "No Friend")
            }
        })

    }
}


//class onePresenter : SearchContract.Presenter {
//
//    private var searchView : SearchContract.View? = null
//
//
//    override fun takeView(view: SearchContract.View) {
//        searchView = view
//        searchView!!.setPresenter(this)
//    }
//
//    override fun buttonClickAction() {
//        searchView?.ShowToast("Success")
//    }
//
//}


