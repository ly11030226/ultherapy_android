package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivityAwaitBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnife
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnifePosition
import com.aimyskin.ultherapy_android.util.createFrameData
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AwaitActivity : BaseActivity() {
    private lateinit var binding: ActivityAwaitBinding
    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false
    private lateinit var receiver: DataReceiver

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
            with(laserSerialService) {
                val result = getFrameDataString(createFrameData())
                this?.sendData(result, 0)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAwaitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            startCircle()
            initData()
            addListener()

            receiver = DataReceiver(object : ReceiveDataCallback {
                override fun parseSuccess(frameBean: FrameBean) {
                    LogUtils.d("frameBean ... $frameBean")
                    LogUtils.d("dataBean ... $DataBean")
                }

                override fun parseFail(message: String) {
                    LogUtils.e("************** parseFail **************")
                }

            })
            val fileIntentFilter = IntentFilter()
            fileIntentFilter.addAction("ACTION_SEND_DATA")
            registerReceiver(receiver, fileIntentFilter)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@AwaitActivity, serviceConnection)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        bound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun initData() {
        if (Profile.isAutoRecognition) {
            DataBean.isAutoRecognition = AutoRecognition.OPEN
        } else {
            DataBean.isAutoRecognition = AutoRecognition.CLOSE
        }
    }

    private fun addListener() {
        //自动感应关闭才能进行点击
        if (!Profile.isAutoRecognition) {
            binding.ivLeft.setOnClickListener {
                currentUseKnife = DataBean.leftHIFU.type
                currentUseKnifePosition = DataBean.leftHIFU.position
                jumpToPickupOrMainActivity()
            }

            binding.ivMiddle.setOnClickListener {
                currentUseKnife = DataBean.middleHIFU.type
                currentUseKnifePosition = DataBean.middleHIFU.position
                jumpToPickupOrMainActivity()
            }

            binding.ivRight.setOnClickListener {
                currentUseKnife = DataBean.rightHIFU.type
                currentUseKnifePosition = DataBean.rightHIFU.position
                jumpToPickupOrMainActivity()
            }
        }

        binding.ivHome.setOnClickListener {
            startActivity(Intent(this@AwaitActivity, IndexActivity::class.java))
            finish()
        }
        binding.ivSetting.setOnClickListener {

        }
    }

    /**
     * 跳转到摘机界面或者治疗界面
     */
    private fun jumpToPickupOrMainActivity() {
        if (Profile.isHaveAnimation) {
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        } else {
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        }
        finish()
    }

    private fun startCircle() {
        Glide.with(this).asGif().load(R.drawable.circle_15).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivLeft)

        Glide.with(this).asGif().load(R.drawable.knife_15).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivMiddle)

        Glide.with(this).asGif().load(R.drawable.knife_30).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivRight)
    }

}