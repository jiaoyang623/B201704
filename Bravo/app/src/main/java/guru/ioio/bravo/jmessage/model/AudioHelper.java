package guru.ioio.bravo.jmessage.model;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by daniel on 17-4-18.
 * record audio
 */

public class AudioHelper implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static int sPlayingId = 0;
    private static SoftReference<AudioHelper> sRef = new SoftReference<>(null);
    private MediaRecorder mRecorder;
    private long mStartTime = 0;
    private long mEndTime = 0;
    private MediaPlayer mMediaPlayer;

    public static boolean downloadPlay(final VoiceContent content, final Message message) {
        if (content == null) {
            return true;
        }

        //先停止
        if (sRef.get() != null) {
            sRef.get().stopPlay();
        }

        //如果正在播放，则停止
        if (sPlayingId == message.getId()) {
            sPlayingId = 0;
            return true;
        }

        sPlayingId = message.getId();
        content.downloadVoiceFile(message, new DownloadCompletionCallback() {
            @Override
            public void onComplete(int i, String s, File file) {
                if (i == 0 && sPlayingId == message.getId()) {
                    AudioHelper helper;
                    if (sRef.get() == null) {
                        helper = new AudioHelper();
                        sRef = new SoftReference<AudioHelper>(helper);
                    } else {
                        helper = sRef.get();
                    }

                    helper.play(file.getAbsolutePath());
                } else {
                    // failed
                }
            }
        });

        return true;
    }

    public void startRecord(String path) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(path);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mRecorder.start();   // Recording is now started
        } catch (IllegalStateException e) {
        }
        mStartTime = System.currentTimeMillis();
    }

    public void stopRecord() {
        if (mRecorder == null) {
            return;
        }
        try {
            mRecorder.stop();
        } catch (Exception e) {
        }
        mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
        mRecorder.release(); // Now the object cannot be reused
        mRecorder = null;
        mEndTime = System.currentTimeMillis();
    }

    public int getDuration() {
        return (int) (mEndTime - mStartTime);
    }

    public void play(String path) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        sPlayingId = 0;
    }
}
