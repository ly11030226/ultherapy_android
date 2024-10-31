package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.drawable.Animatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.ultherapy_android.KEY_NO_CARTIDGE_TYPE
import com.aimyskin.ultherapy_android.NO_CARTIDGE_BOOSTER
import com.aimyskin.ultherapy_android.NO_CARTIDGE_HIFU
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivityNoCartidgeBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.HIFUBean
import com.aimyskin.ultherapy_android.pojo.KnifeState
import com.aimyskin.ultherapy_android.pojo.KnifeUsable
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.util.GlobalVariable
import com.blankj.utilcode.util.LogUtils

/**
 * 提示无刀头页面
 */
class NoCartidgeActivity : BaseActivity() {
    private lateinit var binding: ActivityNoCartidgeBinding
    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false
    private lateinit var receiver: DataReceiver

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoCartidgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initData()
            registerReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData() {
        intent?.let {
            if (it.hasExtra(KEY_NO_CARTIDGE_TYPE)) {
                when (it.getStringExtra(KEY_NO_CARTIDGE_TYPE)) {
                    NO_CARTIDGE_HIFU -> {
                        binding.ivNoCartidge.setBackgroundResource(R.drawable.animation_nocartidge_hifu)
                    }

                    NO_CARTIDGE_BOOSTER -> {
                        binding.ivNoCartidge.setBackgroundResource(R.drawable.animation_nocartidge_booster)
                    }
                }
                startAnimation()
            }
        }
    }

    private fun startAnimation() {
        val rocketAnimation = binding.ivNoCartidge.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.start()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        receiver = DataReceiver(object : ReceiveDataCallback {
            override fun parseSuccess(frameBean: FrameBean) {
                LogUtils.d("frameBean ... $frameBean")
                when (GlobalVariable.currentUseKnifePosition) {
                    Position.LEFT -> {
                        handleData(DataBean.leftHIFU)
                    }
                    Position.MIDDLE -> {
                        handleData(DataBean.middleHIFU)
                    }
                    else -> {
                        handleData(DataBean.rightHIFU)
                    }
                }
            }

            override fun parseFail(message: String) {
                LogUtils.e("************** parseFail **************")
            }
        })
        val fileIntentFilter = IntentFilter()
        fileIntentFilter.addAction("ACTION_SEND_DATA")
        registerReceiver(receiver, fileIntentFilter)
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@NoCartidgeActivity, serviceConnection)
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

    private fun handleData(hifuBean: HIFUBean){
        //手柄不可用，直接跳走
        if(hifuBean.knifeUsable == KnifeUsable.UNUSABLE){
            jumpToAwait()
        }else{
            //手柄可用 并处于挂机状态
            if(hifuBean.knifeState == KnifeState.DOWN){
                jumpToAwait()
            }
            //手柄可用 并处于摘机状态
            else{
                if(hifuBean.type != Type.NONE && hifuBean.type != Type.EMPTY ){
                    //已插入刀头 重新设置刀头的值
                    GlobalVariable.currentUseKnife = hifuBean.type
                    jumpToPickupOrMainActivity()
                }
            }
        }
    }


    private fun jumpToAwait(){
        startActivity(Intent(this@NoCartidgeActivity,AwaitActivity::class.java))
        finish()
    }
    private fun jumpToPickupOrMainActivity() {
        if (Profile.isHaveAnimation) {
            startActivity(Intent(this@NoCartidgeActivity, PickupActivity::class.java))
        } else {
            startActivity(Intent(this@NoCartidgeActivity, MainActivity::class.java))
        }
        finish()
    }
}