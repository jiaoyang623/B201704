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

    public int getMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    public int getVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void setVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public boolean getMute() {
        return false;
    }

    public void setMute(boolean isMute) {
    }
}
