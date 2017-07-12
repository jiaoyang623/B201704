package guru.ioio.helper.model;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by daniel on 7/12/17.
 */

public class AudioHelper {
    private AudioManager mAudioManager;

    public AudioHelper(Context context) {
        //音量控制,初始化定义
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getMaxMusicVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    public int getMusicVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void setMusicVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public int getMaxNotificationVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }


    public int getNotificationVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
    }

    public void setNotificationVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
    }

    public int getMaxAlarmVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }


    public int getAlarmVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    public void setAlarmVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
    }

}
