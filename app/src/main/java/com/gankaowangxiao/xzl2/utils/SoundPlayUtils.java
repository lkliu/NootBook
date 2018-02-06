package com.gankaowangxiao.xzl2.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.gankaowangxiao.xzl2.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaotantan on 17/10/25.
 */

public class SoundPlayUtils {

    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;
    public static Map<String, Integer> map = new HashMap<String, Integer>();
    public static SoundPool mSoundPlayer;
    public static int MAX_SOUNDS = 2;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //SDK_INT >= 21时，才能使用SoundPool.Builder创建SoundPool
            SoundPool.Builder builder = new SoundPool.Builder();

            //可同时播放的音频流
            builder.setMaxStreams(MAX_SOUNDS);

            //音频属性的Builder
            AudioAttributes.Builder attrBuild = new AudioAttributes.Builder();

            //音频类型
            attrBuild.setLegacyStreamType(AudioManager.STREAM_MUSIC);

            builder.setAudioAttributes(attrBuild.build());

            mSoundPlayer = builder.build();
        } else {
            //低版本的构造方法，已经deprecated了
            mSoundPlayer = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }
        // 初始化声音
        mContext = context;
        map.put(Constance.ONE, mSoundPlayer.load(mContext, R.raw.one, 1));
        map.put(Constance.TWO, mSoundPlayer.load(mContext, R.raw.two, 1));
        map.put(Constance.THREE, mSoundPlayer.load(mContext, R.raw.three, 1));
        map.put(Constance.FOUR, mSoundPlayer.load(mContext, R.raw.four, 1));
        map.put(Constance.FIVE, mSoundPlayer.load(mContext, R.raw.five, 1));
        map.put(Constance.SIX, mSoundPlayer.load(mContext, R.raw.six, 1));
        map.put(Constance.SEVEN, mSoundPlayer.load(mContext, R.raw.seven, 1));
        map.put(Constance.EIGHT, mSoundPlayer.load(mContext, R.raw.eight, 1));
        map.put(Constance.NINE, mSoundPlayer.load(mContext, R.raw.nine, 1));
        map.put(Constance.ZERO, mSoundPlayer.load(mContext, R.raw.m_0, 1));
        map.put(Constance.ENTER, mSoundPlayer.load(mContext, R.raw.enter, 1));
        map.put(Constance.BACK, mSoundPlayer.load(mContext, R.raw.back, 1));
        map.put(Constance.SPACE, mSoundPlayer.load(mContext, R.raw.space, 1));
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param
     */
    public static void play(String key) {
        mSoundPlayer.play(map.get(key), 1, 1, 0, 0, 1);
    }

    public static void release() {
        mSoundPlayer.release();
    }
}
