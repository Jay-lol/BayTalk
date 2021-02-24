package com.jay.baytalk.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.InfoManager
import com.jay.baytalk.InfoManager.userName
import com.jay.baytalk.R
import com.jay.baytalk.adapter.RecyclerMessageAdapter
import com.jay.baytalk.BaseActivity
import com.jay.baytalk.databinding.ActivityChatRoomBinding
import com.jay.baytalk.model.data.MessageData
import com.jay.baytalk.viewmodel.MessageRoomViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MessageRoomActivity : BaseActivity() {

    private var rAdapter: RecyclerMessageAdapter? = null
    private var mList: List<MessageData>? = null
    private var userUid: List<String>? = null
    private lateinit var messageRoomViewModel : MessageRoomViewModel
    private lateinit var binding : ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageRoomViewModel = ViewModelProvider(this).get(MessageRoomViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        binding.lifecycleOwner = this
        binding.messageActivity = this

        InfoManager.mIsInForegroundMode = true
        window.setBackgroundDrawableResource(R.drawable.chatroom)

        setAdapter()
        connectMessageServer(intent.getStringExtra("roomId"))
        binding.chatName.text = intent.getStringExtra("roomName")
        userUid = intent.getStringExtra("userUid")?.split(",")
        observeViewModels()
    }

    private fun observeViewModels() {
        messageRoomViewModel.messageLiveData.observe(this, Observer { messageList ->
            showList(messageList)
        })
    }

    private fun setAdapter() {
        binding.messageRecycler.layoutManager = LinearLayoutManager(this)
        rAdapter = RecyclerMessageAdapter(mList, Firebase.auth.currentUser?.uid)
        binding.messageRecycler.adapter = rAdapter
    }

    private fun connectMessageServer(stringExtra: String?) {
        messageRoomViewModel.connectServer(stringExtra)
    }

    fun sendMessageButton(){
        if (binding.content.text.toString() != "") {
            messageRoomViewModel.sendMessage(binding.content.text.toString(), userName, userUid)
        }
        binding.content.text.clear()
    }
    fun functionButton() {
            this.alert("추가 기능은 개발단계에 있습니다", "추가 기능") {
                yesButton {
                }
                noButton {
                }
            }.show()
    }

    /**
     * observeViewModels 로 대체
     */
    private fun showList(message: List<MessageData>) {
        rAdapter ?: return
        rAdapter!!.refresh(message)
        rAdapter!!.notifyDataSetChanged()
        binding.messageRecycler.scrollToPosition(rAdapter!!.itemCount - 1)
    }

    override fun onPause() {
        super.onPause()
        InfoManager.mIsInForegroundMode = false
    }

    override fun onResume() {
        super.onResume()
        InfoManager.mIsInForegroundMode = true
    }

}