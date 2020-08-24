package com.jay.baytalk.view

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.MyCallback
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.R
import com.jay.baytalk.base.BaseActivity
import com.jay.baytalk.model.MessageData
import com.jay.baytalk.model.RecyclerMessageAdapter
import com.jay.baytalk.presenter.RoomConstract
import com.jay.baytalk.presenter.RoomPresenter
import com.jay.baytalk.static
import com.jay.baytalk.static.Companion.userName
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class RoomActivity : BaseActivity(), RoomConstract.View, OnItemClick {
    private var cPresenter : RoomPresenter?  = null
    private var rAdapter : RecyclerMessageAdapter? = null
    private var mList : List<MessageData>? = null
    private var userUid : List<String>? = null
    override fun onPause() {
        super.onPause()
        static.mIsInForegroundMode = false
    }

    override fun onResume() {
        super.onResume()
        static.mIsInForegroundMode = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        static.mIsInForegroundMode = true
        setContentView(R.layout.activity_chat_room)
        window.setBackgroundDrawableResource(R.drawable.chatroom)
        cPresenter?.takeView(this)

        messageRecycler.layoutManager = LinearLayoutManager(this)
        rAdapter = RecyclerMessageAdapter(mList, this, Firebase.auth.currentUser?.uid)
        messageRecycler.adapter = rAdapter

        loadMessage(intent.getStringExtra("roomId"))
        chatName.text = intent.getStringExtra("roomName")
        userUid = intent.getStringExtra("userUid")?.split(",")

        setButton()
    }

    private fun loadMessage(stringExtra: String?) {
        cPresenter?.getMessage(stringExtra, object : MyCallback{
            override fun onCallback(value: List<Any>?) {
                if (value != null) {
                    rAdapter!!.refresh(value as List<MessageData>)
                    rAdapter!!.notifyDataSetChanged()
                    messageRecycler.scrollToPosition(rAdapter!!.itemCount-1)
                } else
                    Log.d("RoomAc" , "No Msg")
            }
        })
    }

    private fun setButton() {
        sendMessage.setOnClickListener {
            if (content.text.toString()!="") {
                cPresenter?.sendMessage(content.text.toString(), userName, userUid)
            }
            content.text.clear()
        }
        function.setOnClickListener{
            this.alert("추가 기능은 개발단계에 있습니다", "추가 기능") {
                yesButton {
                }
                noButton {
                }
            }.show()
        }
    }



    override fun initPresenter() {
        cPresenter = RoomPresenter()
    }

    override fun showList(message: ArrayList<List<String>>) {

    }

    override fun showError(error: String) {

    }

    override fun ShowToast(text: String) {

    }

    override fun setPresenter(presenter: RoomConstract.Presenter) {

    }

    override fun onClick(rid: String, t: Any, i: Int) {

    }
}