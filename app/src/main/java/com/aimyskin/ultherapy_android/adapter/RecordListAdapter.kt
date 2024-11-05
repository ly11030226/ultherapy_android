package com.aimyskin.ultherapy_android.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.inter.ShowRecordListClickCallback
import com.aimyskin.ultherapy_android.inter.ShowUserListClickCallback
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.Record
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder


class RecordListAdapter(private val clickButtonCallback: ShowRecordListClickCallback, private val mName: String, private val mTelephone: String) : BaseQuickAdapter<Record, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Record?) {
        val date: TextView = holder.getView(R.id.item_show_record_date)
        val name: TextView = holder.getView(R.id.item_show_record_name)
        val telephone: TextView = holder.getView(R.id.item_show_record_telephone)
        val therapist: TextView = holder.getView(R.id.item_show_record_therapist)
        val knife: TextView = holder.getView(R.id.item_show_record_knife)
        val number: TextView = holder.getView(R.id.item_show_record_number)
        val ivJump: ImageView = holder.getView(R.id.item_show_record_jump)
        val ivDelete: ImageView = holder.getView(R.id.item_show_record_delete)
        item?.let {
            date.text = it.date
            name.text = mName
            telephone.text = mTelephone
            therapist.text = it.therapist
            knife.text = it.knife
            number.text = it.point.toString()
            ivJump.setOnClickListener {
                clickButtonCallback.clickJump(position)
            }
            ivDelete.setOnClickListener {
                clickButtonCallback.clickDelete(position)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_show_records, parent)
    }
}
