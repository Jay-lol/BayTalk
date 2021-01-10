package com.jay.baytalk.presenter

import android.util.Log
import com.jay.baytalk.contract.MakeChatConstract
import com.jay.baytalk.model.ChatRoomList
import com.jay.baytalk.model.FriendList

class MakeChatPresenter : MakeChatConstract.Presenter {
    private val TAG: String = "로그"
    private var fragmentView : MakeChatConstract.View? = null

    override fun inviteFriend(list: List<List<String>>) {
        val size = list.size
        if (size == 1) {
            fragmentView?.showError("아무도 선택 안할 경우")
            fragmentView?.showToast("최소 1명은 선택하세요!!")
            return
        }
        ChatRoomList.makeChatroom(list){ success ->
            Log.d(TAG, "MakeChatPresenter ~ inviteFriend() called")
            if(!success){
                fragmentView?.showToast("서버 에러로 초대 실패!")
                return@makeChatroom
            }
            fragmentView?.showToast("${size - 1} 명을 초대했습니다")
            fragmentView?.closeView()
        }
    }

    override fun getFriendList() {
        FriendList.loadFriend{ fList ->
            fList?:return@loadFriend
            fragmentView?.setAdapter(fList)
        }
    }

    override fun takeView(view: MakeChatConstract.View) {
        fragmentView = view
    }

    override fun dropView() {
        fragmentView = null
    }
}