package guru.ioio.bravo.jmessage.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import guru.ioio.bravo.base.OneApp;
import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-19.
 */

public class PicturePicker {
    /**
     * 请求码--取照片
     */
    private final int REQUEST_CODE_PHOTO_PICK = 1001;

    /**
     * 请求码--拍照
     */
    private final int REQUEST_CODE_PHOTO_CAMERA = 1002;

    /**
     * 请求码-剪裁照片
     */
    private final int REQUEST_CODE_PHOTO_CROP = 1003;
    /**
     * 裁剪后的宽度（前提是需要裁剪的照片）
     */
    private int mCropWidth = 300;

    /**
     * 裁剪后的高度（前提是需要裁剪的照片）
     */
    private int mCropHeight = 300;

    /**
     * 裁剪比例-x
     */
    private int mCropAspectX = 1;

    /**
     * 裁剪比例-y
     */
    private int mCropAspectY = 1;

    public void select(Activity activity, boolean needCrop) {
        mNeedCrop = needCrop;
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setType("image/*");
            activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_PICK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String mFilePath = null;

    public void take(Activity activity, boolean needCrop) {
        mNeedCrop = needCrop;

        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "toffee_tmp";
        File file = new File(mFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mFilePath)));
        if (activity.getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_CAMERA);
        }
    }

    private boolean mNeedCrop = true;

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (requestCode == REQUEST_CODE_PHOTO_PICK) {//取照片结果
            if (resultCode != Activity.RESULT_CANCELED) {
                Uri uri = data.getData();
                if (uri != null) {
                    if ("content".equals(uri.getScheme())) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        ContentResolver mResolver = OneApp.getInstance().getContentResolver();
                        Cursor cursor = mResolver.query(uri, proj, null, null, null);
                        if (cursor != null) {
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            mFilePath = cursor.getString(column_index);
                            cursor.close();
                        }
                    } else if ("file".equals(uri.getScheme())) {
                        mFilePath = uri.getPath();
                    }
                }
                if (TextUtils.isEmpty(mFilePath)) {
                    if (uri != null && !TextUtils.isEmpty(uri.getPath()) && new File(uri.getPath()).exists()) {
                        mFilePath = uri.getPath();
                    }
                }

                if (TextUtils.isEmpty(mFilePath)) {
                    Utils.toast("从相册获取图片失败");
                    if (mOnPickListener != null) {
                        mOnPickListener.onFailed();
                    }
                    return;
                }

                if (mNeedCrop) {//需要裁剪
                    startCropPhoto(activity);
                    return;
                } else {
                    if (mOnPickListener != null) {
                        mOnPickListener.onSuccess(mFilePath);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_CAMERA) {//拍照结果
            if (resultCode != Activity.RESULT_CANCELED) {
                if (TextUtils.isEmpty(mFilePath)) {
                    Utils.toast("从拍照失败");
                    if (mOnPickListener != null) {
                        mOnPickListener.onFailed();
                    }
                    return;
                }

                if (mNeedCrop) {//需要裁剪
                    startCropPhoto(activity);
                    return;
                } else {
                    if (mOnPickListener != null) {
                        mOnPickListener.onSuccess(mFilePath);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_CROP) {//裁剪结果
            if (resultCode != Activity.RESULT_CANCELED) {
                if (mOnPickListener != null) {
                    mOnPickListener.onSuccess(mFilePath);
                }
            }
        }
    }

    /**
     * 开始裁剪
     */
    private void startCropPhoto(Activity activity) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(new File(mFilePath)), "image/**");//被裁减的图片
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", mCropAspectX);
            intent.putExtra("aspectY", mCropAspectY);
            intent.putExtra("outputX", mCropWidth);
            intent.putExtra("outputY", mCropHeight);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", false);//是否返回Bitmap
            // 用新路径来保存剪裁 后的图片，避免修改系统的图库里的文件
            String newFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "toffee_tmp_crop";
            Uri uriPic = Uri.fromFile(new File(newFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPic);
            activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_CROP);
            mFilePath = newFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            // 若系统不支持裁剪图片，则不进行裁剪
            if (mOnPickListener != null) {
                mOnPickListener.onFailed();
            }
        }
    }


    public interface OnPickListener {
        void onSuccess(String path);

        void onFailed();
    }

    private OnPickListener mOnPickListener;

    public void setOnPickListener(OnPickListener listener) {
        mOnPickListener = listener;
    }
}
