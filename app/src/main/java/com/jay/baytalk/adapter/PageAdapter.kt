package com.jay.baytalk.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jay.baytalk.view.FriendFragment
import com.jay.baytalk.view.RoomListFragment

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var myfg = ArrayList<Fragment>()

//  override fun getItem(position: Int): Fragment = myfg[position]를 좀더 확장
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val friendFragment = FriendFragment()
                // 여기서 프레젠터 주입을 해줘도 됨. takeView가 init으로
                // 프레젠터에서 init으로 바로 할당 FriendPresenter(friendFragment)
                friendFragment
            }
            1 -> {
                val chatFragment = RoomListFragment()
//                RoomListPresenter(chatFragment)
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