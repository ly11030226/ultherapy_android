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
import com.aimyskin.ultherapy_android.pojo.DistanceLength
import com.aimyskin.ultherapy_android.pojo.Pitch
import com.aimyskin.ultherapy_android.pojo.PointOrLine
import com.aimyskin.ultherapy_android.pojo.RepeatTime
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.blankj.utilcode.util.LogUtils

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val startChoiceActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                //发送串口数据
                LogUtils.d("发送串口数据")
                //更新Repeat
                val value = DataBean.repeatTime.byteValue.toFloat() / 10
                binding.tvSingleOrRepeat.text = "Repeat ${value}s"
                //更新Length
                val length = when (DataBean.distanceLength) {
                    DistanceLength.MM5 -> 5
                    DistanceLength.MM10 -> 10
                    DistanceLength.MM15 -> 15
                    DistanceLength.MM20 -> 20
                    DistanceLength.MM25 -> 25
                }
                binding.tvLengthNum.text = "$length"
                //更新Pitch
                val pitch = when(DataBean.pitch){
                    Pitch.MM1_5->1.5
                    Pitch.MM1_6->1.6
                    Pitch.MM1_7->1.7
                    Pitch.MM1_8->1.8
                    Pitch.MM1_9->1.9
                    Pitch.MM2_0->2.0
                }
                binding.tvChoicePitch.text = "$pitch"
            }
        }
    private val startSetupAndInfoActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

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
        binding.ivMainBack.setOnClickListener {
            startActivity(Intent(this@MainActivity, AwaitActivity::class.java))
            finish()
        }

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
                startChoiceActivity.launch(Intent(this@MainActivity, ChoiceRepeatActivity::class.java))
            }
        }
        //设置打点或直线长度
        binding.llLength.setOnClickListener {
            startChoiceActivity.launch(Intent(this@MainActivity, ChoiceLengthActivity::class.java))
        }
        //设置打点或者直线
        binding.llPointOrLine.setOnClickListener {
            //当前打点，改成打直线
            if (DataBean.pointOrLine == PointOrLine.POINT) {
                DataBean.pointOrLine = PointOrLine.LINE
                binding.tvChoicePitch.text = "-"
            }
            //当前打直线，改成打点
            else {
                DataBean.pointOrLine = PointOrLine.POINT
//                binding.tvChoicePitch.text =
            }
        }
        //设置打点间距
        binding.llPitch.setOnClickListener {
            startChoiceActivity.launch(Intent(this@MainActivity, ChoicePitchActivity::class.java))
        }
        //进入设置界面
        binding.llSetup.setOnClickListener {
            startSetupAndInfoActivity.launch(Intent(this@MainActivity, SetupAndInfoActivity::class.java))
        }
    }
}
