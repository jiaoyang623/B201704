package guru.ioio.bravo.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import guru.ioio.bravo.base.OneApp;

/**
 * 检查权限的工具类
 * <p/>
 * Created by wangchenlong on 16/1/26.
 */
public class PermissionsUtils {
    public static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    // 判断权限集合
    public static boolean lacksPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (lacksPermission(permission)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    // 判断是否缺少权限
    private static boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(OneApp.getInstance(), permission) == PackageManager.PERMISSION_DENIED;
    }

    public static void requestPermission(Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 1);

    }
}