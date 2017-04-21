package guru.ioio.bravo.jmessage;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import guru.ioio.bravo.R;
import guru.ioio.bravo.databinding.ActivityChatBinding;
import guru.ioio.bravo.jmessage.model.AudioHelper;
import guru.ioio.bravo.jmessage.model.PicturePicker;
import guru.ioio.bravo.utils.KeyboardHelper;
import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-18.
 * chat list
 */

public class ChatActivity extends Activity implements Comparator<Message>, KeyboardHelper.OnKeyboardChangeListener, PicturePicker.OnPickListener {
    public static final String KEY_TARGET = ChatActivity.class.getName() + ".KEY_TARGET";
    public static final int TYPE_NONE = -1;
    public static final int TYPE_KEYBOARD = 0;
    public static final int TYPE_EMOJI = 1;
    public static final int TYPE_MORE = 2;
    public static final int TYPE_VOICE = 3;

    public static final int RECORD_NONE = 0;
    public static final int RECORD_DO = 1;
    public static final int RECORD_CANCEL = 2;

    public ObservableField<String> input = new ObservableField<>();
    public ObservableInt bottomType = new ObservableInt(TYPE_KEYBOARD);
    public ObservableInt recordingType = new ObservableInt(RECORD_NONE);

    public ChatAdapter adapter;
    public String username;

    private ActivityChatBinding mBinding;
    private KeyboardHelper mKeyboardHelper;
    private PicturePicker mPicker;
    private AudioHelper mAudioHelper;
    private String mAudioPath;
    private BasicCallback mSendCallback = new BasicCallback() {
        @Override
        public void gotResult(int i, String s) {
            if (i == 0 && adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String target = intent.getStringExtra(KEY_TARGET);
        if (TextUtils.isEmpty(target) || JMessageClient.getSingleConversation(target) == null) {
            finish();
            Utils.toast("User not existed!");
            return;
        }

        adapter = new ChatAdapter(getLayoutIdMap());
        adapter.setData(sort(getMessageList(target)));
        username = target;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mBinding.setPresenter(this);

        JMessageClient.enterSingleConversation(username);

        mBinding.list.setSelection(adapter.getCount() - 1);

        mKeyboardHelper = new KeyboardHelper(this, this);
        mPicker = new PicturePicker();
        mPicker.setOnPickListener(this);

        mAudioHelper = new AudioHelper();
    }

    @Override
    protected void onDestroy() {
        JMessageClient.exitConversation();
        super.onDestroy();
    }

    private Map<ContentType, Integer> getLayoutIdMap() {
        Map<ContentType, Integer> map = new HashMap<>();
        map.put(ContentType.text, R.layout.item_chat_txt);
        map.put(ContentType.image, R.layout.item_chat_image);
        map.put(ContentType.voice, R.layout.item_chat_voice);
        return map;
    }

    public List<Message> getMessageList(String target) {
        Conversation conversation = JMessageClient.getSingleConversation(target);
        return conversation.getMessagesFromNewest(0, 999);
    }

    private List<Message> sort(List<Message> list) {
        Collections.sort(list, this);
        return list;
    }

    public boolean onBackClick() {
        finish();
        return true;
    }

    public boolean onPersonClick() {
        //TODO
        return true;
    }

    public boolean onVoiceClick() {
        switchBottomType(TYPE_VOICE);
        return true;
    }

    public boolean onEmojiButtonClick() {
        switchBottomType(TYPE_EMOJI);
        return true;
    }

    public boolean onMoreClick() {
        switchBottomType(TYPE_MORE);
        return true;
    }

    private void switchBottomType(int type) {
        if (bottomType.get() == type) {
            bottomType.set(TYPE_KEYBOARD);
            mBinding.edit.requestFocus();
            KeyboardHelper.show(mBinding.edit);
        } else {
            bottomType.set(type);
            KeyboardHelper.hide(mBinding.edit);
        }
    }

    private void sendText() {
        String content = input.get();
        if (content != null && content.trim().length() > 0) {
            content = content.trim();
            Message message = JMessageClient.createSingleTextMessage(username, content);
            JMessageClient.sendMessage(message);
            adapter.add(message);
            input.set("");
            mBinding.list.setSelection(adapter.getCount() - 1);
        }
    }

    public void afterTextChanged() {
        String txt = input.get();

        if (txt.endsWith("\n")) {
            input.set(txt.trim());
            sendText();
        }
    }

    @Override
    public int compare(Message o1, Message o2) {
        long result = o1.getCreateTime() - o2.getCreateTime();
        return result == 0 ? 0 : (result > 0 ? 1 : -1);
    }

    public void onEmojiClick(String emoji) {
        EditText editText = mBinding.edit;
        int index = editText.getSelectionStart();
        Editable editable = editText.getText();
        editable.insert(index, emoji);
    }

    public void onDeleteClick() {
        mBinding.edit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        mBinding.edit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
    }

    public boolean onSendClick() {
        sendText();
        return true;
    }

    @Override
    public void onKeyboardChange(boolean isOpen) {
        if (isOpen) {
            bottomType.set(TYPE_KEYBOARD);
        }
    }

    public boolean onSelectPhotoClick() {
        mPicker.select(this, false);
        return true;
    }

    public boolean onTakePhotoClick() {
        mPicker.take(this, false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPicker.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onSuccess(String path) {
        //选取图片成功
        try {
            Message msg = JMessageClient.createSingleImageMessage(username, new File(path));
            JMessageClient.sendMessage(msg);
            msg.setOnSendCompleteCallback(mSendCallback);
            adapter.add(msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        switchBottomType(TYPE_NONE);
    }

    @Override
    public void onFailed() {
        //选取图片失败
        Utils.toast("failed");
    }

    public boolean onRecordTouch(View v, MotionEvent event) {
        Utils.j("touch", event.getX(), event.getY(), v.getWidth(), v.getHeight());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                recordingType.set(RECORD_DO);
                startVoiceRecording();
                break;
            case MotionEvent.ACTION_MOVE:
                recordingType.set(isInView(v, event) ? RECORD_DO : RECORD_CANCEL);
                break;
            case MotionEvent.ACTION_UP:
                if (isInView(v, event)) {
                    sendVoiceMessage();
                }
                recordingType.set(RECORD_NONE);
                break;
        }
        return true;
    }

    private boolean isInView(View v, MotionEvent event) {
        int w = v.getWidth();
        int h = v.getHeight();
        float x = event.getX();
        float y = event.getY();
        return (x > 0 && x < w && y > 0 && y < h);
    }

    private void startVoiceRecording() {
        mAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "tmp_audio" + System.currentTimeMillis() + ".amr";
        mAudioHelper.startRecord(mAudioPath);
    }

    private void sendVoiceMessage() {
        mAudioHelper.stopRecord();
        if (mAudioHelper.getDuration() > 2 * 1000) {
            // 超过2s才发送
            try {
                Message msg = JMessageClient.createSingleVoiceMessage(username, new File(mAudioPath), mAudioHelper.getDuration());
                msg.setOnSendCompleteCallback(mSendCallback);
                JMessageClient.sendMessage(msg);
                adapter.add(msg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
