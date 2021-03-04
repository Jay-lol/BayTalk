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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.InfoManager.userName
import com.jay.baytalk.R
import com.jay.baytalk.adapter.RecyclerFriendListAdapter
import com.jay.baytalk.databinding.FragmentFriendBinding
import com.jay.baytalk.extension.showToast
import com.jay.baytalk.model.data.Friend
import com.jay.baytalk.view.init.LoginActivity
import com.jay.baytalk.viewmodel.FriendAndRoomListViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class FriendListFragment : Fragment() {

    private var friendList: List<Friend>? = null
    private lateinit var friendListAdapter: RecyclerFriendListAdapter
    private val  TAG : String = "로그 ${this.javaClass.simpleName}"
    private lateinit var friendAndRoomListViewModel : FriendAndRoomListViewModel
    private lateinit var binding : FragmentFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friendAndRoomListViewModel = ViewModelProvider(requireActivity()).get(FriendAndRoomListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend,  container ,false)
        binding.lifecycleOwner = this
        binding.friendFg = this
        setAdapter()
        loadFriends()
        observeViewModels()
        return binding.root
    }

    private fun setAdapter() {
        friendListAdapter = RecyclerFriendListAdapter()

        binding.friendListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendListRecyclerView.adapter = friendListAdapter
    }

    /**
     * loadFriendList를 변경
     */
    private fun observeViewModels() {
        friendAndRoomListViewModel.friendListLiveData.observe(viewLifecycleOwner , Observer { friendListValues ->
            friendList = friendListValues
            friendList?.let { list ->
                friendListAdapter.submitList(list)
            }
//            friendList?:return@Observer
//            fAdapter.refreshFriendList(friendList!!)
//            fAdapter.notifyDataSetChanged()
        })
    }

    private fun loadFriends() {
        friendAndRoomListViewModel.getFriendList()
    }

    fun logOutButton() {
            activity?.alert(userName + "님 로그아웃 하시겠습니까??", "Logout") {
                yesButton {
                    Firebase.auth.signOut()
                    activity?.startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finishAffinity()
                }
                noButton {

                }
            }?.show()
        this.showToast("Success")
    }
}