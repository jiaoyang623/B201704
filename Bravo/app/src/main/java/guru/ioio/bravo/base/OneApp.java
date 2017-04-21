package guru.ioio.bravo.base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by daniel on 17-4-18.
 * Application
 */

public class OneApp extends Application {
    private static OneApp INSTANCE;

    public OneApp() {
        INSTANCE = this;
    }

    public static OneApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
    }
}
