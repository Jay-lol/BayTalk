package com.jay.baytalk.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.jay.baytalk.OnItemClick
import com.jay.baytalk.R
import kotlinx.android.synthetic.main.recycler_message.view.*
import java.text.SimpleDateFormat

class RecyclerMessageAdapter(
    mList: List<MessageData>?,
    listner: OnItemClick,
    uid: String?
) : RecyclerView.Adapter<RecyclerMessageAdapter.mViewH>() {

    private var mlist = mList
    private val uId = uid
    private val callback = listner

    class mViewH(view: View) : RecyclerView.ViewHolder(view) {
        var rid: Int? = null
        var fname = view.fmessageName
        var fmessage = view.fmessage
        var ftime = view.ftime
        var message = view.message
        var time = view.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewH {
        return mViewH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: mViewH, position: Int) {
        val t = SimpleDateFormat("a K:mm").format(mlist?.get(position)?.time)

        if (uId != null) {

            // friends message
            if (mlist?.get(position)?.userId != uId) {
                holder.message.visibility = View.GONE
                holder.time.visibility = View.GONE
                holder.fmessage.visibility = View.VISIBLE
                val x = holder.fmessage.layoutParams as ConstraintLayout.LayoutParams


                if (position >= 1
                    && mlist?.get(position)?.userId == mlist?.get(position - 1)?.userId
                    && t == SimpleDateFormat("a K:mm").format(mlist?.get(position - 1)?.time)
                ) {

                    holder.fmessage.setBackgroundResource(R.drawable.theme_chatroom_bubble_you_02_image)
                    holder.fname.visibility = View.GONE
                    x.topMargin = 0

                } else {
                    holder.fmessage.setBackgroundResource(R.drawable.theme_chatroom_bubble_you_01_image)
                    holder.fname.visibility = View.VISIBLE
                    x.topMargin = 8
                }

                if (position<mlist!!.size-1 &&
                    mlist?.get(position)?.userId == mlist?.get(position + 1)?.userId &&
                    t == SimpleDateFormat("a K:mm").format(mlist?.get(position + 1)?.time)){
                    holder.ftime.visibility = View.GONE
                } else {
                    holder.ftime.visibility = View.VISIBLE
                }

                holder.fmessage.layoutParams = x
                holder.fmessage.setTextColor(Color.BLACK)
                holder.fname.setTextColor(Color.BLACK)
                holder.fname.text = mlist?.get(position)?.name
                holder.fmessage.text = mlist?.get(position)?.message
                holder.ftime.text = t

            }
            // my message
            else {
                holder.fname.visibility = View.GONE
                holder.fmessage.visibility = View.GONE
                holder.ftime.visibility = View.GONE
                holder.message.visibility = View.VISIBLE
                val x = holder.message.layoutParams as ConstraintLayout.LayoutParams

                if (position >= 1
                    && mlist?.get(position)?.userId == mlist?.get(position - 1)?.userId
                    && (t == SimpleDateFormat("a K:mm").format(mlist?.get(position - 1)?.time))
                ) {
                    holder.message.setBackgroundResource(R.drawable.theme_chatroom_bubble_me_02_image)
                    x.topMargin = 0
                } else {
                    holder.message.setBackgroundResource(R.drawable.theme_chatroom_bubble_me_01_image)
                    x.topMargin = 8
                }

                if (position<mlist!!.size-1 &&
                    uId == mlist?.get(position + 1)?.userId &&
                    t == SimpleDateFormat("a K:mm").format(mlist?.get(position + 1)?.time)){
                    holder.time.visibility = View.GONE
                } else {
                    holder.time.visibility = View.VISIBLE
                }

                holder.message.layoutParams = x
                holder.message.setTextColor(Color.BLACK)
                holder.message.text = mlist?.get(position)?.message
                holder.time.text = t
            }
            setButton()
        }
    }


    private fun setButton() {

    }

    override fun getItemCount(): Int {
        return mlist?.size ?: 0
    }

    fun refresh(newList: List<MessageData>) {
        mlist = newList
    }

}