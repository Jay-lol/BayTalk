package com.jay.baytalk.presenter

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.MyCallback
import com.jay.baytalk.R
import com.jay.baytalk.model.ChatRoomList

class MainPresenter : MainConstract.Presenter {
    private var mView : MainConstract.View? = null
    private val TAG = "MainPresenter"


    override fun welcome(currentUser: FirebaseUser, callback: MyCallback) {

        val myRef = Firebase.database
            .getReference("Users/${currentUser.uid}/name")
        // Read from the database

        //addValueEventListener를 활용하여 채팅방을 구현해야한다.  onDataChange는 데이터가 변경될 때마다 호출되기 때문
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                callback.onCallback(listOf(value) as List<Any>)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun takeView(view: MainConstract.View) {
        mView = view
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
