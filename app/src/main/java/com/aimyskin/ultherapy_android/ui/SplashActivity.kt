package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.MediaController
import android.widget.Toast
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivitySplashBinding
import com.blankj.utilcode.util.LogUtils

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var videoMediaPlayer: MediaPlayer
    private lateinit var audioMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initVideo()
            initAudio()
//            Handler(Looper.getMainLooper()).postDelayed({
//                videoMediaPlayer.release()
//                stopAudio()
//                startActivity(Intent(this@SplashActivity, AwaitActivity::class.java))
//                finish()
//            }, 8000)

        } catch (e: Exception) {
            e.printStackTrace()
        }
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

}