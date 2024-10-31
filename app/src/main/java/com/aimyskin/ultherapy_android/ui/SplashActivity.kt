package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.MediaController
import android.widget.Toast
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivitySplashBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.util.createFrameData
import com.aimyskin.ultherapy_android.util.pxToDp
import com.blankj.utilcode.util.LogUtils

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var videoMediaPlayer: MediaPlayer
    private lateinit var audioMediaPlayer: MediaPlayer
    private lateinit var receiver: DataReceiver
    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
            with(laserSerialService) {
                val result = getFrameDataString(createFrameData(command = Command.WRITE.byteValue.toByte()))
                this?.sendData(result, 0)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initVideo()
            initAudio()
            registerReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
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
    }

    private fun initVideo() {
        // 视频播放器设置
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash_video)
        binding.vvSplash.setVideoURI(videoUri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.vvSplash)
        binding.vvSplash.setMediaController(mediaController)

        binding.vvSplash.setOnPreparedListener { mp ->
            videoMediaPlayer = mp
            videoMediaPlayer.start()
            // 开始播放音频
            startAudio()
        }

        binding.vvSplash.setOnErrorListener { mp, what, extra ->
            Toast.makeText(this, "视频播放错误", Toast.LENGTH_SHORT).show()
            false // 返回false表示不处理错误，让系统处理
        }

        binding.vvSplash.setOnCompletionListener {
            try {
                stopAudio()
                binding.vvSplash.stopPlayback();
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, AwaitActivity::class.java))
                    finish()
                }, 50)
            } catch (e: IllegalStateException) {
                LogUtils.e("VideoView", "Error releasing VideoView: " + e.message);
            }
        }
    }

    private fun initAudio() {
        // 音频播放器设置
        val audioUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.poweron)
        audioMediaPlayer = MediaPlayer.create(this, audioUri).apply {
            setOnErrorListener { mp, what, extra ->
                Toast.makeText(this@SplashActivity, "音频播放错误", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun startAudio() {
        try {
            audioMediaPlayer.start()
        } catch (e: Exception) {
            Toast.makeText(this, "音频播放失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopAudio() {
        audioMediaPlayer.stop()
        audioMediaPlayer.release()
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@SplashActivity, serviceConnection)
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

}