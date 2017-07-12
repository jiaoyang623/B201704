package guru.ioio.helper.base;

import android.app.Application;


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
    }
}
