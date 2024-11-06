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
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.list.listItems
import com.aimyskin.ultherapy_android.DEFAULT_THERAPIST
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.REMINDER_COMPLETE_CONTENT
import com.aimyskin.ultherapy_android.REMINDER_STANdBY_STATE
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityRegisterBinding
import com.aimyskin.ultherapy_android.pojo.Gender
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.util.getCurrentDateStr
import com.aimyskin.ultherapy_android.viewmodel.AddUserViewModel
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 注册用户页面
 */
@Suppress("NAME_SHADOWING")
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val genderList = arrayOf(Gender.FEMALE, Gender.MALE)
    private val addUserViewModel: AddUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            addListener()
            initObserver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initObserver() {
        addUserViewModel.run {
            addUserLiveData.observe(this@RegisterActivity, Observer {
                if (it.isSuccess) {
                    Toasty.success(this@RegisterActivity, it.message).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toasty.error(this@RegisterActivity, it.message).show()
                }
            })
        }
    }

    @SuppressLint("CheckResult")
    private fun addListener() {
        binding.ivRegisterBack.setOnClickListener {
            setResult(RESULT_CANCELED)
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
            val name = binding.etRegisterName.text.toString()
            val gender = binding.tvRegisterGenderValue.text.toString()
            val birth = binding.tvRegisterBirthdayValue.text.toString()
            val telephone = binding.etRegisterTelephone.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            if (name.isNotEmpty() && gender.isNotEmpty() && birth.isNotEmpty() && telephone.isNotEmpty() && email.isNotEmpty()) {
                val user = User(name, gender, getCurrentDateStr(), birth, telephone, email, DEFAULT_THERAPIST)
                addUserViewModel.addUserToLocal(user)
            } else {
                Toasty.warning(this@RegisterActivity, REMINDER_COMPLETE_CONTENT, Toast.LENGTH_SHORT, true).show()
            }
        }
    }
}