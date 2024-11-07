package com.aimyskin.ultherapy_android.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.inter.ShowUserListClickCallback
import com.aimyskin.ultherapy_android.pojo.User
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

class UserListAdapter(private val clickButtonCallback: ShowUserListClickCallback) : BaseQuickAdapter<User, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: User?) {
        val id: TextView = holder.getView(R.id.item_show_users_id)
        val registerDate: TextView = holder.getView(R.id.item_show_users_registertime)
        val name: TextView = holder.getView(R.id.item_show_users_name)
        val telephone: TextView = holder.getView(R.id.item_show_users_telephone)
        val therapist: TextView = holder.getView(R.id.item_show_users_therapist)
        val ivSave: ImageView = holder.getView(R.id.item_show_users_save)
        val ivDelete: ImageView = holder.getView(R.id.item_show_users_delete)
        item?.let {
            id.text = it.userId.toString()
            registerDate.text = it.rDate
            name.text = it.name
            telephone.text = it.telephone
            therapist.text = it.therapist
            ivSave.setOnClickListener { v ->
                ClickSoundPoolUtils.play(v.context, R.raw.click)
                clickButtonCallback.clickSave(position)
            }
            ivDelete.setOnClickListener { v ->
                ClickSoundPoolUtils.play(v.context, R.raw.click)
                clickButtonCallback.clickDelete(position)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_show_users, parent)
    }
}
