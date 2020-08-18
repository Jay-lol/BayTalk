package com.jay.baytalk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.model.FriendList
import com.jay.baytalk.model.RecyclerMakeRoomListAdapter
import com.jay.baytalk.static.Companion.frag
import com.jay.baytalk.view.MainActivity
import kotlinx.android.synthetic.main.fragment_make_room.view.*
import java.security.acl.Group


class MakeChatroomFragment : Fragment(), OnItemClick {

    private var mAdapter : RecyclerMakeRoomListAdapter? = null
    private var inviteList = mutableListOf<List<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_make_room, container, false)
        frag = this

        view.recyclerViewMakeroom.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = RecyclerMakeRoomListAdapter(FriendList.getFriendlistData(), this)
        view.recyclerViewMakeroom.adapter = mAdapter

        val bundle = arguments
        if (bundle!=null) {
            Log.d("asdasdas", bundle.getStringArrayList("key")?.get(0).toString())
            bundle.getStringArrayList("key")?.let { inviteList.add(listOf(
                it[0], it[1]
            )) }
        } else
            Log.d("asadasdas", "null")
        clickButton(view)

        return view
    }

    private fun clickButton(view: View?) {
        view?.inviteButton?.setOnClickListener{
            val size = inviteList.size
            if(size!=1) {
                Toast.makeText(context, "${size - 1} 명을 초대했습니다", Toast.LENGTH_LONG).show()
                val nTime = System.currentTimeMillis()
                val roomName = "${inviteList[0][0]}@$nTime@${size}방"
                // 방장아이디@방만든시간@방인원
                val database = Firebase.database
                lateinit var myRef: DatabaseReference
                val x = mutableListOf<String>()
                var userNameList = ""
                for (i in inviteList.indices) {
                    if (i != size-1)
                        userNameList += inviteList[i][1] + ","
                    else
                        userNameList += inviteList[i][1]
                    x.add(inviteList[i][0])
                }

                for (i in inviteList) {
                    myRef = database.getReference("RoomUser/${i[0]}/$roomName")
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["lastMessage"] = " "
                    hashMap["roomId"] = roomName
                    if (size > 2) hashMap["roomType"] = "Group" else hashMap["roomType"] = "Private"
                    hashMap["time"] = nTime
                    hashMap["userName"] = userNameList
                    hashMap["userUid"] = x
                    myRef.setValue(hashMap)
                }

                x.clear()

                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.beginTransaction()?.remove(this)?.commit()
                fragmentManager?.popBackStack()
            } else {
                Toast.makeText(context, "최소 1명은 선택하세요!!", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onClick(rid: String ,t: Any, i : Int) {
        if (i ==1)
            inviteList.add(listOf(rid, t.toString()))
        else
            inviteList.remove(listOf(rid, t.toString()))
    }

}