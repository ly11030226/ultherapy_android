package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.list.listItems
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.REMINDER_COMPLETE_CONTENT
import com.aimyskin.ultherapy_android.REMINDER_STANdBY_STATE
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityRegisterBinding
import com.aimyskin.ultherapy_android.pojo.Gender
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import es.dmoral.toasty.Toasty

/**
 * 注册用户页面
 */
@Suppress("NAME_SHADOWING")
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val genderList = arrayOf(Gender.FEMALE, Gender.MALE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {

    }

    @SuppressLint("CheckResult")
    private fun addListener() {
        binding.ivRegisterBack.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, IndexActivity::class.java))
            finish()
        }
        binding.tvRegisterGenderValue.setOnClickListener {
            MaterialDialog(this).show {
                listItems(R.array.gender) { dialog, index, text ->
                    binding.tvRegisterGenderValue.text = text
                }
            }
        }
        binding.tvRegisterBirthdayValue.setOnClickListener {
            MaterialDialog(this).show {
                datePicker { dialog, date ->
                    val date = getString(R.string.choice_birthday).format(date.year, date.month + 1, date.dayOfMonth)
                    binding.tvRegisterBirthdayValue.text = date
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etRegisterName.text
            val gender = binding.tvRegisterGenderValue.text
            val birth = binding.tvRegisterBirthdayValue.text
            val telephone = binding.etRegisterTelephone.text
            val email = binding.etRegisterEmail.text
            if (name!!.isNotEmpty() && gender!!.isNotEmpty() && birth!!.isNotEmpty() && telephone!!.isNotEmpty() && email!!.isNotEmpty()) {

            } else {
                Toasty.warning(this@RegisterActivity, REMINDER_COMPLETE_CONTENT, Toast.LENGTH_SHORT, true).show()
            }
        }
    }
}