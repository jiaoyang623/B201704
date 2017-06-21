package guru.ioio.whisky.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import guru.ioio.whisky.base.OneApp;


/**
 * Created by daniel on 17-4-18.
 * utils
 */

public class Utils {
    public static final long TIME = System.currentTimeMillis();
    private final static SimpleDateFormat DATE_FORMAT_0 = new SimpleDateFormat("MM月dd日");
    private final static SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("yy年MM月dd日");
    private final static SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyMMdd_HHmmss");

    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
    private final static String[] constellationArr = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    public static void toast(String message) {
        Toast.makeText(OneApp.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String tag, Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }
        if (tag == null) {
            tag = "log";
        }

        StringBuilder builder = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                builder.append("null");
            } else if (obj instanceof Class) {
                builder.append(((Class) obj).getSimpleName());
            } else {
                builder.append(obj.toString());
            }
            builder.append(", ");
        }

        Log.i(tag, builder.toString());
    }

    public static void j(Object... objects) {
        log("jy", objects);
    }

    public static int dip2px(float dpValue) {
        final float scale = OneApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = OneApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void copy(InputStream in, String outPath) {
        File f = new File(outPath);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(in);
            closeQuietly(fos);
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
    }

    public static void closeQuietly(InputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeQuietly(OutputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一分钟以内发的消息——显示“刚刚”
     * 一分钟以上到一个小时以内——显示“xx分钟前”
     * 1小时以上24小时以内——显示“xx小时前”
     * 24小时以上-48小时以内——1天前
     * 48小时以上72小时以内——2天前
     * ……
     * <p>
     * 7天以上——具体到xx月xx日
     * 大于1年——yyyy年xx月xx日
     *
     * @param timestamp
     * @return
     */

    public static String makeDate(long timestamp) {
        long curTime = System.currentTimeMillis();
        long tmp = (curTime - timestamp);
        if (tmp < 60 * 1000) {
            return tmp / (60 * 1000) + "分钟前";
        } else if (tmp < 24 * 60 * 1000) {
            return tmp / (24 * 60 * 1000) + "小时前";
        } else if (tmp < 7 * 24 * 60 * 1000) {
            return tmp / (7 * 24 * 60 * 1000) + "天前";
        } else if ((tmp < 365 * 7 * 24 * 60 * 1000) && new Date().getYear() == new Date(timestamp).getYear()) {
            return DATE_FORMAT_0.format(new Date(timestamp));
        } else {
            return DATE_FORMAT_2.format(new Date(timestamp));
        }
    }

    public static String makeDateFileName(long timestamp) {
        return DATE_FORMAT_1.format(new Date(timestamp));
    }

    /**
     * 根据生日得到星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
    }

    public static long adaptTime(long time) {
        return TIME / time > 10 ? time * 1000 : time;
    }

    public static boolean isBackground() {
        Context context = OneApp.getInstance().getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean getManifestParameter(String key) {
        try {
            Context context = OneApp.getInstance();
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getBoolean(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String sha1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isLowBuidVersion() {
        return Build.VERSION.SDK_INT <= 19;
    }

    public static void deleteDirAsync(final String path) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                deleteDir(path);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void deleteDir(String path) {
        deleteDir(new File(path));
    }

    public static void deleteDir(File f) {
        if (!f.exists()) {
            return;
        } else {
            if (f.isDirectory()) {
                for (File i : f.listFiles()) {
                    deleteDir(i);
                }

            }

            f.delete();
        }
    }

    public static String arrayToString(ArrayList<?> arrayList) {
        if (arrayList == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : arrayList) {
            stringBuilder.append(String.valueOf(object) + ",");
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> stringToArray(String str) {
        ArrayList<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] strs = str.split(",");
            if (strs != null && strs.length > 0) {
                for (int i = 0; i < strs.length; i++) {
                    list.add(strs[i]);
                }
            }
        }
        return list;
    }

    public static ArrayList<Integer> stringToIntArray(String str) {
        ArrayList<Integer> list = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] strs = str.split(",");
            if (strs != null && strs.length > 0) {
                for (int i = 0; i < strs.length; i++) {
                    try {
                        list.add(Integer.parseInt(strs[i]));
                    } catch (Exception e) {

                    }
                }
            }
        }
        return list;
    }

    public static String getNumPostfix(int number) {
        if (number > 10 && number < 20) {
            return "th";
        }
        switch (number % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getDCIM() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }


    public static String getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) OneApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getTypeName();
        } else {
            return "unknown";
        }
    }

    public static boolean isSizeFit(int w0, int h0, int w1, int h1) {
        return w0 * w1 != 0 && Math.abs(h0 * 1.0 / w0 - h1 * 1.0 / w1) < 0.3;
    }

    public static void setBoldText(TextView v, boolean isBold) {
        v.getPaint().setFakeBoldText(isBold);
    }

    public static String numToStr(int num) {
        if (num < 10000) {
            return String.valueOf(num);
        } else if (num < 10000 * 10000) {
            return String.format("%.1fw", num / 10000f);
        } else {
            return "9999+w";
        }
    }

    public static void printStackTrace() {
        new Exception().printStackTrace();
    }

    public static boolean doNothing() {
        return true;
    }

    public static void gotoAppDetailSetting(Context context) {
        try {
            Intent settingIntent = new Intent();
            settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            settingIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            settingIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(settingIntent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;

    /**
     * 屏幕真正高度，去掉底部触摸屏
     */
    public static int realScreenHeight;

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getWidth() {
        if (screenWidth == 0) {
            screenWidth = OneApp.getInstance().getResources().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    public static int getCameraDisplayOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        activity = null;
        return degrees;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getHeight() {
        if (screenHeight == 0) {
            screenHeight = OneApp.getInstance().getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    /**
     * 获得标题栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> mClass = null;
        Object obj = null;
        Field field = null;
        int resID = 0, height = 0;
        try {
            mClass = Class.forName("com.android.internal.R$dimen");
            obj = mClass.newInstance();
            field = mClass.getField("status_bar_height");
            resID = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(resID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取屏幕真正高度，去掉底部触摸屏
     *
     * @return
     */
    public static int getRealHeight() {

        if (realScreenHeight == 0) {

            WindowManager w = (WindowManager) OneApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
            // since SDK_INT = 1;
            realScreenHeight = metrics.heightPixels;
            // includes window decorations (statusbar bar/navigation bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
                try {
                    realScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
                // includes window decorations (statusbar bar/navigation bar)
            } else if (Build.VERSION.SDK_INT >= 17) {
                try {
                    android.graphics.Point realSize = new android.graphics.Point();
                    Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(d, realSize);
                    realScreenHeight = realSize.y;
                } catch (Exception ignored) {
                }
            }
        }
        return realScreenHeight;
    }

}
