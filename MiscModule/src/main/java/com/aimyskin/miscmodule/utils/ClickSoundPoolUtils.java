package com.aimyskin.miscmodule.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.RawRes;

public class ClickSoundPoolUtils {

    private static ClickSoundPoolUtils utils;
    private SoundPool mSoundPool = null;

    public static ClickSoundPoolUtils getInstance() {
        if (utils == null) {
            utils = new ClickSoundPoolUtils();
        }
        return utils;
    }

    public ClickSoundPoolUtils() {
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(1);
        spb.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        mSoundPool = spb.build();
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
    }

    public static void play(Context context, @RawRes int raw) {
        int soundId = ClickSoundPoolUtils.getInstance().mSoundPool.load(context, raw, 1);
    }

    public static void play(Context context, String rawPath) {
        int soundId = ClickSoundPoolUtils.getInstance().mSoundPool.load(rawPath, 1);
    }
}
