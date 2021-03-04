package com.jay.baytalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.adapter.diffutil.FriendDiffUtil
import com.jay.baytalk.databinding.RecyclerMakeroomBinding
import com.jay.baytalk.model.data.Friend

class RecyclerMakeRoomListAdapter(val callback: (String, String, Int) -> Unit) :
    ListAdapter<Friend, RecyclerMakeRoomListAdapter.MakeRoomViewHolder>(FriendDiffUtil) {

    inner class MakeRoomViewHolder(val view: RecyclerMakeroomBinding) : RecyclerView.ViewHolder(view.root) {

        fun onBind(item: Friend, position: Int) {
            view.checkBox.isChecked = false
            if (position == 0) {
                view.checkBox.visibility = View.GONE
            } else {
                view.checkBox.visibility = View.VISIBLE
            }
            view.Mname.text = item.name
            setButton(item.rid, view.checkBox, item.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeRoomViewHolder {
        return MakeRoomViewHolder(
            RecyclerMakeroomBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MakeRoomViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    private fun setButton(rid: String?, checkBox: CheckBox, name: String) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) callback(rid!!, name, 1)
            else callback(rid!!, name, 2)
        }
    }

}