package com.jay.baytalk.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.InfoManager
import com.jay.baytalk.InfoManager.userName
import com.jay.baytalk.R
import com.jay.baytalk.adapter.RecyclerFriendListAdapter
import com.jay.baytalk.base.BaseFragment
import com.jay.baytalk.contract.FriendConstract
import com.jay.baytalk.extension.showToaster
import com.jay.baytalk.model.data.Friend
import com.jay.baytalk.presenter.FriendPresenter
import com.jay.baytalk.view.init.LoginActivity
import kotlinx.android.synthetic.main.fragment_friend.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class FriendFragment : BaseFragment(), FriendConstract.View {

    private lateinit var mPresenter: FriendConstract.Presenter
    private var friendList: List<Friend>? = null
    private var fAdapter: RecyclerFriendListAdapter? = null
    private val TAG = "FriendFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        view.friendListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        fAdapter = RecyclerFriendListAdapter(friendList)
        view.friendListRecyclerView.adapter = fAdapter

        loadFriends()

        setButton(view)

        return view
    }

    private fun loadFriends() {
        mPresenter.getFriendList()
    }

    override fun loadFriendList(value : List<Friend>){
        friendList = value
        friendList?.let { it1 -> fAdapter!!.refreshFriendList(it1) }
        fAdapter!!.notifyDataSetChanged()
        InfoManager.activity?.finish()
    }

    private fun setButton(view: View) {
        view.FriendButton.setOnClickListener {
            activity?.alert(userName + "님 로그아웃 하시겠습니까??", "Logout") {
                yesButton {
                    Firebase.auth.signOut()
                    activity?.startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finishAffinity()
                }
                noButton {

                }
            }?.show()
            mPresenter.buttonClickAction()
        }

    }

    override fun showList(list: List<Any>) {
        Log.d("showList", "not")
    }

    override fun showError(error: String) {
        Log.d("showError", error)
    }

    override fun showToast(msg: String) {
        Log.d(TAG, "is clicked? showToast")
        this.showToaster(msg)
    }

    override fun initPresenter() {
        mPresenter = FriendPresenter()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
    }

}