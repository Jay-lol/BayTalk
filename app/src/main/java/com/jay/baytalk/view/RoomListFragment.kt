package com.jay.baytalk.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.baytalk.InfoManager.userData
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.R
import com.jay.baytalk.model.data.ChatRoom
import com.jay.baytalk.adapter.RecyclerChatRoomListAdapter
import com.jay.baytalk.extension.showToaster
import com.jay.baytalk.presenter.RoomListConstract
import kotlinx.android.synthetic.main.fragment_chat.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class RoomListFragment : Fragment(), RoomListConstract.View, OnItemClick {

    private lateinit var mPresenter: RoomListConstract.Presenter
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
            makeRoom()
        }
    }

    private fun makeRoom(){
        val fragmentManager = requireActivity().supportFragmentManager
        val newFragment = MakeChatroomFragment()
        newFragment.arguments = bundleOf(Pair("key", userData))
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.do_nothing,
            R.anim.do_nothing,
            R.anim.enter_from
        )
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadRoom() {
        mPresenter.getChatList()
    }

    override fun showList(list: MutableList<ChatRoom>) {
        cAdapter!!.refresh(list)
        cAdapter!!.notifyDataSetChanged()
    }

    override fun showError(error: String) {
        Log.d("showError", error)
    }

    override fun showToast(msg: String) {
        this.showToaster(msg)
    }

    override fun setPresenter(presenter: RoomListConstract.Presenter) {
        mPresenter = presenter
    }

    override fun onChatRoomDelete(rid: String) {
        activity?.alert("채팅방을 나가시겠습니까??", "나가기") {
            yesButton {
                mPresenter.deleteChatRoom(rid)
            }
            noButton {
            }
        }?.show()
    }

    override fun onChatroomClick(rid: String, chatName: String, userUids: List<String>) {
        val intent = Intent(context, RoomActivity::class.java)
        intent.putExtra("roomId", rid)
        intent.putExtra("roomName", chatName)
        var useruid = ""
        for (i in 0 until (userUids.size-1)){
            useruid += userUids[i]+","
        }
        useruid += userUids[userUids.size-1]
        intent.putExtra("userUid", useruid)
        context?.startActivity(intent)
    }
}