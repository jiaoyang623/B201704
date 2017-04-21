package guru.ioio.bravo.utils;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import guru.ioio.bravo.base.OneApp;

/**
 * Created by daniel on 17-4-18.
 * utils
 */

public class Utils {
    private final static SimpleDateFormat DATE_FORMAT_0 = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

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
            builder.append(obj == null ? "null" : obj.toString()).append(", ");
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

    public static String makeDate(long timestamp) {
        return DATE_FORMAT_0.format(new Date(timestamp));
    }
}
