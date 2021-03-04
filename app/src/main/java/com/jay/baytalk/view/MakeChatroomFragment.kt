package com.jay.baytalk.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.baytalk.InfoManager.frag
import com.jay.baytalk.R
import com.jay.baytalk.adapter.RecyclerMakeRoomListAdapter
import com.jay.baytalk.databinding.FragmentMakeRoomBinding
import com.jay.baytalk.extension.showToast
import com.jay.baytalk.viewmodel.FriendAndRoomListViewModel

class MakeChatroomFragment : Fragment() {

    private val TAG: String = "로그"

    private var mAdapter: RecyclerMakeRoomListAdapter? = null
    private var inviteList = mutableListOf<List<String>>()
    private lateinit var binding: FragmentMakeRoomBinding
    private lateinit var friendAndRoomListViewModel: FriendAndRoomListViewModel

    private val MY_PERSONAL_KEY = "key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * this로 해서 뷰모델 공유 실패. requireActivity로 수정
         */
        friendAndRoomListViewModel =
            ViewModelProvider(requireActivity()).get(FriendAndRoomListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_room, container, false)
        binding.lifecycleOwner = this
        binding.makeRoomFg = this
        binding.root.isClickable = true
        frag = this

        setAdapter()
        observeViewModels()

        return binding.root
    }

    private fun observeViewModels() {
        friendAndRoomListViewModel.inviteSuccessMessage.observe(viewLifecycleOwner , Observer { successMessage ->
            if (successMessage=="INIT") return@Observer
            this.showToast(successMessage)
            closeView()
        })
        friendAndRoomListViewModel.friendListLiveData.observe(viewLifecycleOwner , Observer { friendListValues ->
            mAdapter?.submitList(friendListValues)
        })
    }

    fun inviteButton() {
        val size = inviteList.size
        if (size <= 1) {
           Log.d(TAG, "inviteButton() called 아무도 선택 안할 경우")
            this.showToast("최소 1명은 선택하세요!!")
            return
        }
        friendAndRoomListViewModel.inviteFriend(inviteList)
    }

    private fun closeView() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
        fragmentManager.popBackStack()
    }

    private fun setAdapter() {
        mAdapter = RecyclerMakeRoomListAdapter { rid, name, check ->
            if (check == 1)
                inviteList.add(listOf(rid, name))
            else
                inviteList.remove(listOf(rid, name))
        }

        binding.recyclerViewMakeroom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMakeroom.adapter = mAdapter
        // 자기자신도 추가
        arguments?.getStringArrayList(MY_PERSONAL_KEY)?.let{ myData ->
            Log.d(TAG, myData[0])
            inviteList.add(listOf(myData[0], myData[1]))
        }
    }
}