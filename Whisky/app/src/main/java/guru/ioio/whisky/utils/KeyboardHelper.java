package guru.ioio.whisky.utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import guru.ioio.whisky.base.OneApp;


/**
 * Created by daniel on 3/18/17.
 * 键盘事件监听类
 */

public class KeyboardHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    public static View mview;
    public static ObservableBoolean isEditing = new ObservableBoolean(false);
    private Activity mActivity;
    private OnKeyboardChangeListener mListener;
    private int mLastHeight = 0;
    private Rect mRect = new Rect();

    public KeyboardHelper(Activity activity, OnKeyboardChangeListener listener) {
        mActivity = activity;
        mListener = listener;

        mActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public static void show(View view) {
        mview = view;
        InputMethodManager imm = (InputMethodManager) OneApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hide(View view) {
        InputMethodManager imm = (InputMethodManager) OneApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        mview = null;
    }

    public static void hide() {
        if (mview != null) {
            InputMethodManager imm = (InputMethodManager) OneApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mview.getWindowToken(), 0); //强制隐藏键盘
            mview = null;
        }

    }

    public void release() {
        mActivity = null;
    }

    @Override
    public void onGlobalLayout() {
        if (mActivity == null) {
            return;
        }
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
        int h = mRect.bottom;
        int offset = Utils.getHeight() - h;
        if (mLastHeight == 0) {
        } else if (mLastHeight > h) {
            mListener.onKeyboardChange(true, offset);
        } else if (mLastHeight < h) {
            mListener.onKeyboardChange(false, offset);
        }

        mLastHeight = h;
    }

    public static int getOffset(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return Utils.getHeight() - rect.bottom;
    }

    public interface OnKeyboardChangeListener {
        void onKeyboardChange(boolean isOpen, int offset);
    }
}
