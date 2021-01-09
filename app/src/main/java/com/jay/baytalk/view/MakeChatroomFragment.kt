package com.jay.baytalk.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jay.baytalk.adapter.RecyclerMakeRoomListAdapter
import com.jay.baytalk.base.BaseFragment
import com.jay.baytalk.extension.showToaster
import com.jay.baytalk.model.data.Friend
import com.jay.baytalk.presenter.makechatroom.MakeChatConstract
import com.jay.baytalk.presenter.makechatroom.MakeChatPresenter
import com.jay.baytalk.InfoManager.frag
import com.jay.baytalk.R
import kotlinx.android.synthetic.main.fragment_make_room.view.*


class MakeChatroomFragment : BaseFragment(), MakeChatConstract.View {

    private val TAG: String = "로그"

    private lateinit var presenter : MakeChatConstract.Presenter

    private var mAdapter: RecyclerMakeRoomListAdapter? = null
    private var inviteList = mutableListOf<List<String>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_make_room, container, false)
        view.isClickable = true
        frag = this

        presenter.getFriendList()
        setUpButton(view)

        return view
    }

    override fun initPresenter() {
        Log.d(TAG, "MakeChatroomFragment ~ initPresenter() called")
        presenter = MakeChatPresenter()
        presenter.takeView(this)
    }

    private fun setUpButton(view: View?) {
        view?.inviteButton?.setOnClickListener {
            presenter.inviteFriend(inviteList)
        }
    }

    override fun setPresenter(presenter: MakeChatConstract.Presenter) {
        super.setPresenter(presenter)
    }

    override fun closeView(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
        fragmentManager.popBackStack()
    }

    override fun setAdapter(fList: List<Friend>) {
        view ?: return
        view!!.recyclerViewMakeroom.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = RecyclerMakeRoomListAdapter(fList) { rid, name, check ->
            if (check == 1)
                inviteList.add(listOf(rid, name))
            else
                inviteList.remove(listOf(rid, name))
        }
        view!!.recyclerViewMakeroom.adapter = mAdapter
        // 자기자신도 추가
        val bundle = arguments
        bundle?.getStringArrayList("key") ?: return
        Log.d(TAG, bundle.getStringArrayList("key")!![0].toString())
        val mydata = bundle.getStringArrayList("key")!!
        inviteList.add(listOf( mydata[0], mydata[1] ))
    }

    override fun showToast(msg: String) {
        this.showToaster(msg)
    }

    override fun showError(error: String) {
        Log.e(TAG, "showError: $error")
    }
}