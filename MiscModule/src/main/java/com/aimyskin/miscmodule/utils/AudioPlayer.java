package com.aimyskin.miscmodule.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.aimyskin.miscmodule.R;

@Deprecated
public class AudioPlayer implements MediaPlayer.OnPreparedListener {

    private static AudioPlayer audioPlayer;

    public static AudioPlayer getAudioPlayer() {
        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer();
        }
        return audioPlayer;
    }

    private MediaPlayer mediaPlayer;

    public void playAudio(Context context, String audioPath) {
        playAudio(context, audioPath, false, null);
    }

    public void playAudio(Context context, String audioPath, MediaPlayer.OnCompletionListener onCompletionListener) {
        playAudio(context, audioPath, false, onCompletionListener);
    }

    public void playAudio(Context context, String audioPath, boolean isLoop, MediaPlayer.OnCompletionListener onCompletionListener) {
        stopAudio();
        mediaPlayer = new MediaPlayer();
        try {
            if (audioPath.startsWith("android.resource://")) {
//                String audioPath = "android.resource://" + getPackageName() + "/" + R.raw.your_audio_resource;
                Uri rawUri = Uri.parse(audioPath);
                mediaPlayer.setDataSource(context, rawUri);
            } else {
//                String audioPath = "/sdcard/your_folder/your_audio_file.mp3";
                mediaPlayer.setDataSource(audioPath);
            }
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setLooping(isLoop);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            stopAudio();
        }
    }

    public void stopAudio() {
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
