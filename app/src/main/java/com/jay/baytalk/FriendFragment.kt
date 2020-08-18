package com.jay.baytalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.model.Friend
import com.jay.baytalk.model.RecyclerFriendListAdapter
import com.jay.baytalk.presenter.FriendConstract
import com.jay.baytalk.static.Companion.userName
import com.jay.baytalk.view.LoginActivity
import kotlinx.android.synthetic.main.fragment_friend.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class FriendFragment : Fragment(), FriendConstract.View {

    private var mPresenter: FriendConstract.Presenter? = null
    private var friendList : List<Friend>? = null
    private var fAdapter : RecyclerFriendListAdapter? = null
    private val TAG = "FriendFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
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
        mPresenter?.getFriendList(object : MyCallback {
            override fun onCallback(value: List<Any>?) {
                friendList = value as List<Friend>
                friendList?.let{ it1-> fAdapter!!.refresh(it1)}
                fAdapter!!.notifyDataSetChanged()
//                static.activity?.finish()
            }
        })
    }

    private fun setButton(view : View){

        view.FriendButton.setOnClickListener{
            activity?.alert(userName+"님 로그아웃 하시겠습니까??", "Logout"){
                yesButton {
                    Firebase.auth.signOut()
                    activity?.startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finishAffinity()
                }
                noButton {

                }
            }?.show()
            mPresenter?.buttonClickAction()
        }

    }

    override fun showList(list: List<Any>) {
        Log.d("showList", "not")
    }

    override fun showError(error: String) {
        Log.d("showError", error)
    }

    override fun ShowToast(text: String) {
        Log.d("is clicked?", "ShowToast")
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: FriendConstract.Presenter) {
        mPresenter = presenter
    }

}