package com.aimyskin.ultherapy_android.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityMainBinding
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.PointOrLine
import com.aimyskin.ultherapy_android.pojo.RepeatTime
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.blankj.utilcode.util.LogUtils

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                //发送串口数据
                LogUtils.d("发送串口数据")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        //根据currentUseKnife设置中上部的刀头logo
        binding.miv.initImage()

    }

    private fun addListener() {
        //更换 single 或者 repeat
        binding.ivSingleOrRepeat.setOnClickListener {
            //当前是单向，需转变为重复模式
            if (DataBean.singleOrRepeat == SingleOrRepeat.SINGLE) {
                DataBean.singleOrRepeat = SingleOrRepeat.REPEAT
                binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_repeat)
                val value = DataBean.repeatTime.byteValue.toFloat() / 10
                LogUtils.d("value ... $value")
                binding.tvSingleOrRepeat.text = "Repeat ${value}s"
            }
            //当前是重复模式，需转变为单向
            else {
                DataBean.singleOrRepeat = SingleOrRepeat.SINGLE
                binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_single)
                binding.tvSingleOrRepeat.text = "Single"
            }
        }
        //设置repeat的时间
        binding.tvSingleOrRepeat.setOnClickListener {
            //当前是单向，需转变为重复模式
            if (DataBean.singleOrRepeat == SingleOrRepeat.SINGLE) {
                DataBean.singleOrRepeat = SingleOrRepeat.REPEAT
                binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_repeat)
                val value = DataBean.repeatTime.byteValue.toFloat() / 10
                LogUtils.d("value ... $value")
                binding.tvSingleOrRepeat.text = "Repeat ${value}s"
            } else {
                startActivity.launch(Intent(this@MainActivity, ChoiceRepeatActivity::class.java))
            }
        }
        //设置打点或直线长度
        binding.llLength.setOnClickListener {

        }
        //设置打点或者直线
        binding.llPointOrLine.setOnClickListener {
            //当前打点，改成打直线
            if(DataBean.pointOrLine == PointOrLine.POINT){
                DataBean.pointOrLine = PointOrLine.LINE
                binding.tvChoicePitch.text = "-"
            }
            //当前打直线，改成打点
            else{
                DataBean.pointOrLine = PointOrLine.POINT
//                binding.tvChoicePitch.text =
            }
        }
        //设置打点间距
        binding.llPitch.setOnClickListener {

        }
        //进入设置界面
        binding.llSetup.setOnClickListener {

        }
    }
}
