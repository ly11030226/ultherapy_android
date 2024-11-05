package com.aimyskin.ultherapy_android.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.inter.ShowUserListClickCallback
import com.aimyskin.ultherapy_android.pojo.Gender
import com.aimyskin.ultherapy_android.pojo.User
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

class UserManagerListAdapter() : BaseQuickAdapter<User, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: User?) {
        val profile: ImageView = holder.getView(R.id.iv_user_manager_list_profile)
        val name: TextView = holder.getView(R.id.tv_user_manager_list_name)
        val telephone: TextView = holder.getView(R.id.tv_user_manager_list_telephone)
        val date: TextView = holder.getView(R.id.tv_user_manager_list_date)
        val therapist: TextView = holder.getView(R.id.tv_user_manager_list_therapist)

        item?.let {
            if (it.gender == Gender.FEMALE.gender) {
                profile.setBackgroundResource(R.drawable.icon_female)
            } else {
                profile.setBackgroundResource(R.drawable.icon_male)
            }
            name.text = it.name
            telephone.text = it.telephone
            date.text = it.rDate
            therapist.text = "Therapist: ${it.therapist}"
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_user_manager_list, parent)
    }
}
