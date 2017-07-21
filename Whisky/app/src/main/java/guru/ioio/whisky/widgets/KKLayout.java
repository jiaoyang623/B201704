package guru.ioio.whisky.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by daniel on 6/21/17.
 * for layout like emoji
 */

public class KKLayout extends FrameLayout {
    public static final String TYPE_KEYBOARD = "keyboard";
    public static final String TYPE_INPUT = "input";
    private int mOffset = 0;

    private KeyboardStatus mStatus;

    public enum KeyboardStatus {
        None, InnerKeyboard, SoftKeyboard
    }

    public KKLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public KKLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KKLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KKLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mOffset = dip2px(context, 300);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOffset != 0) {
            for (int i = getChildCount() - 1; i != -1; i--) {
                View v = getChildAt(i);
                if (TYPE_KEYBOARD.equals(v.getTag())) {
                    measureChild(v, widthMeasureSpec, MeasureSpec.makeMeasureSpec(mOffset, MeasureSpec.EXACTLY));
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int offset = getOffset();

        if (offset > 0) {
            if (!isRequestShowInnerKeyboard) {
                hideAllInnerKeyboards();
            }
        } else {
            isRequestShowInnerKeyboard = false;
        }

        for (int i = getChildCount() - 1; i != -1; i--) {
            View v = getChildAt(i);
            if (TYPE_KEYBOARD.equals(v.getTag())) {
                layoutKeyboard(v, left, top, right, bottom, offset);
            } else if (TYPE_INPUT.equals(v.getTag())) {
                layoutInput(v, left, top, right, bottom, offset);
            } else {
                layoutContent(v, left, top, right, bottom, offset);
            }
        }

        changeStatus(offset);
    }

    private void changeStatus(int offset) {
        if (offset > 0) {
            // softkeyboard
            if (mStatus != KeyboardStatus.SoftKeyboard) {
                mStatus = KeyboardStatus.SoftKeyboard;
                if (mListener != null) {
                    mListener.onKeyboardChange(mStatus);
                }
            }
        } else if (isInnerKeyboardVisible()) {
            // inner keyboard
            if (mStatus != KeyboardStatus.InnerKeyboard) {
                mStatus = KeyboardStatus.InnerKeyboard;
                if (mListener != null) {
                    mListener.onKeyboardChange(mStatus);
                }
            }
        } else {
            // none
            if (mStatus != KeyboardStatus.None) {
                mStatus = KeyboardStatus.None;
                if (mListener != null) {
                    mListener.onKeyboardChange(mStatus);
                }
            }
        }
    }


    private void layoutKeyboard(View v, int left, int top, int right, int bottom, int offset) {
        if (offset > 0) {
            v.layout(left, bottom, right, bottom + mOffset);
        } else {
            v.layout(left, bottom - mOffset, right, bottom);
        }
    }

    private void layoutInput(View v, int left, int top, int right, int bottom, int offset) {
        if (offset > 0) {
            v.layout(left, bottom - v.getMeasuredHeight(), right, bottom);
        } else if (isInnerKeyboardVisible()) {
            int b = bottom - mOffset;
            v.layout(left, b - v.getMeasuredHeight(), right, b);
        } else {
            v.layout(left, bottom - v.getMeasuredHeight(), right, bottom);
        }
    }

    private void layoutContent(View v, int left, int top, int right, int bottom, int offset) {
        if (offset > 0) {
            // 键盘可见
            layoutWithMargin(v, left, top, right, bottom);
        } else if (isInnerKeyboardVisible()) {
            // 内置键盘可见
            layoutWithMargin(v, left, top, right, bottom - mOffset);
        } else {
            // 无键盘
            layoutWithMargin(v, left, top, right, bottom);
        }
    }

    private void layoutWithMargin(View v, int left, int top, int right, int bottom) {
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        if (params != null) {
            v.layout(left + params.leftMargin, top + params.topMargin, right - params.rightMargin, bottom - params.bottomMargin);
        } else {
            v.layout(left, top, right, bottom);
        }
    }

    public boolean isInnerKeyboardVisible() {
        for (int i = getChildCount() - 1; i != -1; i--) {
            View v = getChildAt(i);
            if (TYPE_KEYBOARD.equals(v.getTag())) {
                if (v.getVisibility() == VISIBLE) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKeyboardVisible() {
        return getOffset() != 0 || isInnerKeyboardVisible();
    }


    private Rect mRect = new Rect();
    private int mScreenHeight = 0;

    private int getOffset() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
            if (mScreenHeight == 0) {
                mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
            }
            int offset = mScreenHeight - mRect.bottom;
            if (offset != 0) {
                mOffset = offset;
            }
            return offset;
        } else {
            throw new IllegalArgumentException("Activity context is in need");
        }
    }

    public void showSoftKeyboard(EditText edit, boolean isShow) {
        if (edit == null) {
            return;
        }

        if (isShow) {
            edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
        } else {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private boolean isRequestShowInnerKeyboard = false;

    public void showInnerKeyboard(EditText edit, boolean isShow, int innerKeyboardResourceId) {
        View inner;
        if (edit == null || (inner = findViewById(innerKeyboardResourceId)) == null || !TYPE_KEYBOARD.equals(inner.getTag())) {
            return;
        }
        if (isShow) {
            edit.requestFocus();
            inner.setVisibility(VISIBLE);
            // 隐藏软键盘
            showSoftKeyboard(edit, false);
            isRequestShowInnerKeyboard = true;
        } else {
            inner.setVisibility(GONE);
        }
    }

    private void hideAllInnerKeyboards() {
        View v;
        for (int i = getChildCount() - 1; i != -1; i--) {
            if (TYPE_KEYBOARD.equals((v = getChildAt(i)).getTag())) {
                v.setVisibility(GONE);
            }
        }
    }

    private OnKeyboardChangeListener mListener;

    public void setOnKeyboardChangeListener(OnKeyboardChangeListener l) {
        mListener = l;
    }

    public interface OnKeyboardChangeListener {
        void onKeyboardChange(KeyboardStatus status);
    }
}
