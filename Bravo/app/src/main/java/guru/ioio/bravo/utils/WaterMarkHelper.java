package guru.ioio.bravo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by daniel on 6/5/17.
 * water mark
 */

public class WaterMarkHelper {
    private static final String UNZIP_DIR = "wm";

    private String getDir(Context context) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + UNZIP_DIR;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    public void initialize(Context context) {
        String dir = getDir(context) + File.separator;
        InputStream in = null;
        ZipInputStream zin = null;
        FileOutputStream fout = null;
        try {
            in = context.getAssets().open("water_mark.zip");
            zin = new ZipInputStream(in);
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                fout = new FileOutputStream(dir + name);
                Utils.copy(zin, fout);
                Utils.closeQuietly(fout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(in);
            Utils.closeQuietly(zin);
            Utils.closeQuietly(fout);
        }
    }

    public void release(Context context) {
        File d = new File(getDir(context));
        if (d.exists()) {
            for (File f : d.listFiles()) {
                f.delete();
            }
            d.delete();
        }
    }

    public Bitmap createWaterMark(Context context, String logo, String id) {
        String dir = getDir(context) + File.separator;
        return createWaterMark(decode(dir + logo), decode(dir + "wm_id.png"), decode(dir + "wm_num.png"), id);
    }

    public Bitmap decode(String path) {
        return BitmapFactory.decodeFile(path);
    }


    public void makeWaterMark(Context context, String id, String dir) {
        checkDir(dir);
        for (int i = 0; i < 30; i++) {
            String path = dir + File.separator + "wm_" + id + "_" + i++ + ".png";
            save(path, createWaterMark(context, "water_mark/wm_" + i + ".png", id), id);
        }
    }

    private void checkDir(String path) {
        File d = new File(path);
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    public void save(String path, Bitmap bitmap, String id) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(fout);
        }
    }

    // num: 133*19
    // logo: 150*50
    // id: 31*23

    public Bitmap createWaterMark(Bitmap logoBitmap, Bitmap idBitmap, Bitmap numBitmap, String id) {
        List<Integer> indexList = new ArrayList<>();
        for (char c : id.toCharArray()) {
            int index = c - '0';
            if (index >= 0 && index <= 9) {
                indexList.add(index);
            }
        }
        int subHeight = Math.max(numBitmap.getHeight(), idBitmap.getHeight());
        int numWidth = indexList.size() * numBitmap.getWidth() / 10;
        int subWidth = idBitmap.getWidth() + numWidth;

        int height = logoBitmap.getHeight() + subHeight;
        int width = Math.max(logoBitmap.getWidth(), subWidth);

        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(target);
        //Draw logo
        canvas.drawBitmap(logoBitmap, (width - logoBitmap.getWidth()) / 2, 0, paint);
        //Draw id
        canvas.drawBitmap(idBitmap, (width - subWidth) / 2, logoBitmap.getHeight() + (subHeight - idBitmap.getHeight()) / 2, paint);
        //Draw num
        int w = numBitmap.getWidth() / 10;
        int h = numBitmap.getHeight();
        int numLeft = (width - subWidth) / 2 + idBitmap.getWidth();
        int numTop = logoBitmap.getHeight() + (subHeight - numBitmap.getHeight()) / 2;

        Rect src = new Rect(0, 0, 0, numBitmap.getHeight());
        Rect dst = new Rect(0, numTop, 0, numTop + h);

        int pos = 0;
        for (int i : indexList) {
            src.left = i * w;
            src.right = src.left + w;
            dst.left = numLeft + w * pos;
            dst.right = dst.left + w;

            canvas.drawBitmap(numBitmap, src, dst, paint);
            pos++;
        }

        return target;
    }

}
