package guru.ioio.bravo.jmessage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import guru.ioio.bravo.R;
import guru.ioio.bravo.base.OneApp;
import guru.ioio.bravo.databinding.ActivityConfigBinding;
import guru.ioio.bravo.jmessage.model.PicturePicker;
import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-18.
 * Config chat
 */

public class ConfigActivity extends Activity implements PicturePicker.OnPickListener {

    public ObservableField<String> username = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> currentUser = new ObservableField<>();
    public ObservableField<String> chatname = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    private PicturePicker mPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfigBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_config);
        binding.setPresenter(this);
        refreshMyName();
        mPicker = new PicturePicker();
        mPicker.setOnPickListener(this);
    }

    private void refreshMyName() {
        if (JMessageClient.getMyInfo() != null) {
            currentUser.set(JMessageClient.getMyInfo().getUserName());
        } else {
            currentUser.set("no name");
        }
    }

    public boolean onRegisterClick() {
        JMessageClient.register(username.get(), password.get(), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //success
                    Utils.toast("register success");
                } else {
                    //failed
                    Utils.toast("register failed");
                    Utils.j(responseDesc);
                }
                isLoading.set(false);
            }
        });
        isLoading.set(true);
        return true;
    }

    public boolean onLoginClick() {
        JMessageClient.login(username.get(), password.get(), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //success
                    Utils.toast("register success");
                } else {
                    //failed
                    Utils.toast("register failed");
                    Utils.j(responseDesc);
                }

                refreshMyName();

                isLoading.set(false);
            }
        });

        isLoading.set(true);

        return true;
    }

    public boolean onLogoutClick() {
        JMessageClient.logout();
        refreshMyName();

        return true;
    }

    public boolean onChatClick() {
        JMessageClient.sendMessage(JMessageClient.createSingleTextMessage(chatname.get(), "hello"));
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_TARGET, chatname.get());
        startActivity(intent);

        return true;
    }

    public boolean onSelectClick() {
        mPicker.select(this, true);
        return true;
    }

    public boolean onTakeClick() {
        mPicker.take(this, true);
        return true;
    }

    public boolean hack() {
//        final String path = Environment.getExternalStorageDirectory().getAbsoluteFile().getAbsolutePath() + File.separator + "hack_tmp";
        final String path = OneApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "hack_tmp";
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    FileOutputStream fos = new FileOutputStream(path);
                    fos.write("hack".getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileInputStream fis = new FileInputStream(path);
                    byte[] buffer = new byte[32];
                    int count = fis.read(buffer);
                    fis.close();
                    System.out.println(new String(buffer, 0, count));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPicker.onActivityResult(requestCode, resultCode, data, this);
    }


    @Override
    public void onSuccess(String path) {
        Utils.j("setResult", path);
        if (TextUtils.isEmpty(path)) {
            onFailed();
            return;
        }
        File f = new File(path);
//        if (f.exists()) {
        JMessageClient.updateUserAvatar(f, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Utils.toast("success");
                } else {
                    Utils.toast("failed");
                }
            }
        });
//        } else {
//            Utils.toast("file not existed");
//        }
    }

    @Override
    public void onFailed() {
        Utils.toast("no picture selected");
    }
}
