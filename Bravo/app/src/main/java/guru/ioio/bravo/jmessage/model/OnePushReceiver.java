package guru.ioio.bravo.jmessage.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-21.
 */

public class OnePushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.toast(intent.getAction());
    }
}
