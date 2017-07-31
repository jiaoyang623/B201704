package guru.ioio.whisky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import guru.ioio.whisky.base.Utils;

/**
 * Created by daniel on 7/31/17.
 * test for user
 */

public class UnlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Utils.toast("ACTION_SCREEN_ON");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Utils.toast("ACTION_SCREEN_OFF");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Utils.toast("ACTION_USER_PRESENT");
        }
    }
}
