package com.jay.baytalk.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.baytalk.InfoManager.userData
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.R
import com.jay.baytalk.adapter.RecyclerChatRoomListAdapter
import com.jay.baytalk.databinding.FragmentRoomBinding
import com.jay.baytalk.model.data.ChatRoom
import com.jay.baytalk.viewmodel.FriendAndRoomListViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class RoomListFragment : Fragment(), OnItemClick {

    private var chatRoomListAdapter: RecyclerChatRoomListAdapter? = null
    private val  TAG : String = "로그 ${this.javaClass.simpleName}"
    private lateinit var binding : FragmentRoomBinding
    private lateinit var friendAndRoomListViewModel : FriendAndRoomListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friendAndRoomListViewModel = ViewModelProvider(requireActivity()).get(FriendAndRoomListViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room, container, false)
        binding.lifecycleOwner = this
        binding.roomFg = this

        setAdapter()
        loadRoom()
        observeViewModels()

        return binding.root
    }

    /**
     * chatRoomList를  livedata를 관찰해서 가져온다.
     */
    private fun observeViewModels() {
        friendAndRoomListViewModel.chatRoomListLiveData.observe(viewLifecycleOwner , Observer { roomListValues ->
            chatRoomListAdapter?.submitList(roomListValues)
        })
    }

    private fun loadRoom() {
        friendAndRoomListViewModel.getChatList()
    }

    private fun setAdapter() {
        chatRoomListAdapter = RecyclerChatRoomListAdapter(this)

        binding.recyclerViewChatRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChatRoom.adapter = chatRoomListAdapter
    }

    fun makeRoom(){
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

    override fun onChatRoomDelete(rid: String) {
        activity?.alert("채팅방을 나가시겠습니까??", "나가기") {
            yesButton {
                friendAndRoomListViewModel.deleteChatRoom(rid)
            }
            noButton {
            }
        }?.show()
    }

    override fun onChatroomClick(rid: String, chatName: String, userUids: List<String>) {
        val intent = Intent(context, MessageRoomActivity::class.java)
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