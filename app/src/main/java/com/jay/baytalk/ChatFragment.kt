package com.jay.baytalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.baytalk.model.ChatRoom
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.RecyclerChatRoomListAdapter
import com.jay.baytalk.presenter.ChatConstract
import com.jay.baytalk.presenter.FriendConstract
import com.jay.baytalk.view.LoginActivity
import com.jay.baytalk.view.MainActivity
import com.jay.baytalk.view.RoomActivity
import kotlinx.android.synthetic.main.fragment_chat.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class ChatFragment : Fragment(), ChatConstract.View, OnItemClick {

    private var mPresenter: ChatConstract.Presenter? = null
    private var myChatRoomList: List<ChatRoom>? = null
    private var cAdapter: RecyclerChatRoomListAdapter? = null
    private val TAG = "ChatroomFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        view.recyclerViewChatRoom.layoutManager = LinearLayoutManager(requireContext())
        cAdapter = RecyclerChatRoomListAdapter(myChatRoomList, this)
        view.recyclerViewChatRoom.adapter = cAdapter

        loadRoom()

        setUpButton(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    private fun setUpButton(view: View?) {
        view?.makeChatRoom?.setOnClickListener {
//            val databasefe = Firebase.database
//            var myReffe = databasefe.getReference("message")
//            val ll = ChatRoom("make","더존","단톡방")
//            val zz = mapOf<String,String>(Pair("ket","pp"))
////            myReffe.child("abcdefg").push().setValue(zz)  //abc아래 랜덤 아래 zz값들
//            myReffe.child("zxcvb").push().setValue(true)
//            myReffe.child("zxzxcvb").push().setValue("abc")
            (activity as MainActivity).makeRoom()
        }
    }

    private fun loadRoom() {
        mPresenter?.getChatList(object : MyCallback {
            override fun onCallback(value: List<Any>?) {
                if (value != null) {
                    cAdapter!!.refresh(value as List<ChatRoom>)
                    cAdapter!!.notifyDataSetChanged()
                } else
                    Log.d(TAG, "No Friend")
            }
        })
    }


    override fun showList(list: List<Any>) {
        Log.d("showList", "not")
    }

    override fun showError(error: String) {
        Log.d("showError", error)
    }

    override fun ShowToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: ChatConstract.Presenter) {
        mPresenter = presenter
    }

    override fun onClick(rid: String, t: Any, i: Int) {
        if (i == 1) {
            activity?.alert("채팅방을 나가시겠습니까??", "나가기") {
                yesButton {
                    mPresenter?.deleteChatRoom(rid)
                }
                noButton {
                }
            }?.show()
        }
    }

    override fun onClick2(rid: String, t: Any, x: List<String>) {
        val intent = Intent(context, RoomActivity::class.java)
        intent.putExtra("roomId", rid)
        intent.putExtra("roomName", t as String)
        var useruid = ""
        for (i in 0 until (x.size-1)){
            useruid += x[i]+","
        }
        useruid += x[x.size-1]
        intent.putExtra("userUid", useruid)
        context?.startActivity(intent)
    }

}