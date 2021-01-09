package com.jay.baytalk.presenter

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.R
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.FriendList

class MainPresenter : MainConstract.Presenter {
    private var searchView : MainConstract.View? = null
    private val TAG = "MainPresenter"


    override fun welcome(currentUser: FirebaseUser) {
        FriendList.loadMyInfo(currentUser){ name ->
            searchView?.welcomeMent(name)
        }
    }

    override fun takeView(view: MainConstract.View) {
        searchView = view
    }

    fun setFcm(context: Context) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = context.getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                token?.let{ChatRoomList.sendFcmId(it)}

            })
    }


}
