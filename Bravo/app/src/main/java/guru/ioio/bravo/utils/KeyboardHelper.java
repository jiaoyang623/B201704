package guru.ioio.bravo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import guru.ioio.bravo.base.OneApp;


/**
 * Created by daniel on 3/18/17.
 * 键盘事件监听类
 */

public class KeyboardHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    private Activity mActivity;
    private OnKeyboardChangeListener mListener;

    public KeyboardHelper(Activity activity, OnKeyboardChangeListener listener) {
        mActivity = activity;
        mListener = listener;

        mActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private int mLastHeight = 0;
    private Rect mRect = new Rect();

    @Override
    public void onGlobalLayout() {
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
        int h = mRect.bottom;

        if (mLastHeight == 0) {
        } else if (mLastHeight > h) {
            mListener.onKeyboardChange(true);
        } else if (mLastHeight < h) {
            mListener.onKeyboardChange(false);
        }

        mLastHeight = h;
    }

    public interface OnKeyboardChangeListener {
        void onKeyboardChange(boolean isOpen);
    }

    public static void show(View view) {
        InputMethodManager imm = (InputMethodManager) OneApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hide(View view) {
        InputMethodManager imm = (InputMethodManager) OneApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
}
