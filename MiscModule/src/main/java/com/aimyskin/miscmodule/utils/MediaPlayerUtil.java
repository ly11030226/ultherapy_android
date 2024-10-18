package com.aimyskin.miscmodule.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 音频播放工具类（使用时new MediaPlayerUtil()保证mediaplayer不会被回收）
 */
@Deprecated
public class MediaPlayerUtil implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void play(Context context, int resId, boolean isLoop, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setLooping(isLoop);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }
    public void play(Context context, int resId){
        play(context , resId , false , null );
    }

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
