package com.jay.baytalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.R
import com.jay.baytalk.model.data.Friend
import kotlinx.android.synthetic.main.recycler_makeroom.view.*

class RecyclerMakeRoomListAdapter(fList : List<Friend>?,
                                  val callback : (String ,String ,Int) -> Unit)
    : RecyclerView.Adapter<RecyclerMakeRoomListAdapter.MakeRoomViewHolder>() {

    private var flist = fList

    inner class MakeRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rid : Int? = null
        var name = view.Mname
        var checkBox = view.checkBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeRoomViewHolder {
        return MakeRoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_makeroom, parent, false))
    }

    override fun onBindViewHolder(holder: MakeRoomViewHolder, position: Int) {
        holder.checkBox.isChecked = false
        if (position==0)
            holder.checkBox.visibility = View.GONE
        else
            holder.checkBox.visibility = View.VISIBLE

        holder.name.text = flist?.get(position)?.name
        setButton(flist?.get(position)?.rid,holder.checkBox, holder.name.text.toString())
    }

    private fun setButton(rid: String?, checkBox: CheckBox, name: String) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) callback(rid!! ,name, 1)
            else callback(rid!! ,name, 2)
        }
    }

    override fun getItemCount(): Int {
        return flist?.size ?: 0
    }
}