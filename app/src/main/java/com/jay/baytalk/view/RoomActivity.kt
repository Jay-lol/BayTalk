package com.jay.baytalk.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.R
import com.jay.baytalk.base.BaseActivity
import com.jay.baytalk.model.data.MessageData
import com.jay.baytalk.adapter.RecyclerMessageAdapter
import com.jay.baytalk.presenter.RoomConstract
import com.jay.baytalk.presenter.RoomPresenter
import com.jay.baytalk.InfoManager
import com.jay.baytalk.InfoManager.userName
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class RoomActivity : BaseActivity(), RoomConstract.View {
    private var cPresenter: RoomPresenter? = null
    private var rAdapter: RecyclerMessageAdapter? = null
    private var mList: List<MessageData>? = null
    private var userUid: List<String>? = null
    override fun onPause() {
        super.onPause()
        InfoManager.mIsInForegroundMode = false
    }

    override fun onResume() {
        super.onResume()
        InfoManager.mIsInForegroundMode = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InfoManager.mIsInForegroundMode = true
        setContentView(R.layout.activity_chat_room)
        window.setBackgroundDrawableResource(R.drawable.chatroom)
        cPresenter?.takeView(this)

        setAdapter()

        loadMessage(intent.getStringExtra("roomId"))
        chatName.text = intent.getStringExtra("roomName")
        userUid = intent.getStringExtra("userUid")?.split(",")

        setButton()
    }

    private fun setAdapter() {
        messageRecycler.layoutManager = LinearLayoutManager(this)
        rAdapter = RecyclerMessageAdapter(mList, Firebase.auth.currentUser?.uid)
        messageRecycler.adapter = rAdapter
    }

    private fun loadMessage(stringExtra: String?) {
        cPresenter?.getMessage(stringExtra)
    }

    private fun setButton() {
        sendMessage.setOnClickListener {
            if (content.text.toString() != "") {
                cPresenter?.sendMessage(content.text.toString(), userName, userUid)
            }
            content.text.clear()
        }
        function.setOnClickListener {
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

    override fun showList(message: List<MessageData>) {
        rAdapter?:return
        rAdapter!!.refresh(message)
        rAdapter!!.notifyDataSetChanged()
        messageRecycler.scrollToPosition(rAdapter!!.itemCount - 1)
    }

    override fun showError(error: String) {

    }

    override fun showToast(msg: String) {

    }

    override fun setPresenter(presenter: RoomConstract.Presenter) {

    }

}