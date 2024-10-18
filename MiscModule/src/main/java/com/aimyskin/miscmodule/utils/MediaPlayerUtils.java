package com.aimyskin.miscmodule.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

public class MediaPlayerUtils implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "MediaPlayerUtils";
    private static MediaPlayerUtils utils;
    private MediaPlayer mediaPlayer;

    public static MediaPlayerUtils getInstance() {
        if (utils == null) {
            utils = new MediaPlayerUtils();
        }
        return utils;
    }

    public MediaPlayerUtils() {
        mediaPlayer = new MediaPlayer();
    }

    public void playAudio(Context context, String audioPath) {
        playAudio(context, audioPath, false, null, null, null);
    }

    public void playAudio(Context context, String audioPath, boolean isLoop) {
        playAudio(context, audioPath, isLoop, null, null, null);
    }

    /**
     * @param audioPath //  String audioPath = "android.resource://" + getPackageName() + "/" + R.raw.your_audio_resource;
     *                  String audioPath = "/sdcard/your_folder/your_audio_file.mp3";
     * @param isLoop    循环播放
     */
    public void playAudio(Context context, String audioPath, boolean isLoop, MediaPlayer.OnPreparedListener onPreparedListener,
                          MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnErrorListener onErrorListener) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer.setLooping(isLoop);
        mediaPlayer.setOnPreparedListener(onPreparedListener == null ? this : onPreparedListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener == null ? this : onCompletionListener);
        mediaPlayer.setOnErrorListener(onErrorListener == null ? this : onErrorListener);
        try {
            if (audioPath.startsWith("android.resource://")) {
                Uri rawUri = Uri.parse(audioPath);
                mediaPlayer.setDataSource(context, rawUri);
            } else {
                mediaPlayer.setDataSource(audioPath);
            }
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 播放完成
        if (mp != null){
            if (!mp.isLooping()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // 准备完成后开始播放
        if (mp != null) {
            mp.start();
        }
    }
}
