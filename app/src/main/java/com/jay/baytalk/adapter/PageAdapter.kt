package com.jay.baytalk.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jay.baytalk.ChatFragment
import com.jay.baytalk.FriendFragment
import com.jay.baytalk.presenter.ChatPresenter
import com.jay.baytalk.presenter.FriendPresenter

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var myfg = ArrayList<Fragment>()

//  override fun getItem(position: Int): Fragment = myfg[position]를 좀더 확장
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val friendFragment = FriendFragment()
                FriendPresenter(friendFragment)
                friendFragment
            }
            1 -> {
                val chatFragment = ChatFragment()
                ChatPresenter(chatFragment)
                chatFragment
            }
            else -> myfg[position]
        }
    }

    override fun getCount(): Int = myfg.size

    fun addItem(fragment : Fragment){
        myfg.add(fragment)
    }
}