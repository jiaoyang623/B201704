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
import android.widget.FrameLayout;

/**
 * Created by daniel on 6/21/17.
 * for layout like emoji
 */

public class KKLayout extends FrameLayout {
    public static final String TYPE_KEYBOARD = "keyboard";
    public static final String TYPE_INPUT = "input";
    private int mOffset = 0;

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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = getChildCount() - 1; i != -1; i--) {
            View v = getChildAt(i);
            if (TYPE_KEYBOARD.equals(v.getTag())) {
                layoutKeyboard(v, left, top, right, bottom);
            } else if (TYPE_INPUT.equals(v.getTag())) {
                layoutInput(v, left, top, right, bottom);
            }
        }
    }


    private void layoutKeyboard(View v, int left, int top, int right, int bottom) {
        int offset = getOffset();
        if (offset > 0) {
            v.layout(left, bottom, right, bottom + mOffset);
        } else {
            v.layout(left, bottom - mOffset, right, bottom);
        }
    }

    private void layoutInput(View v, int left, int top, int right, int bottom) {
        int offset = getOffset();

        if (offset > 0) {
            v.layout(left, bottom - v.getMeasuredHeight(), right, bottom);
        } else if (isInnerKeyboardVisible()) {
            int b = bottom - mOffset;
            v.layout(left, b - v.getMeasuredHeight(), right, b);
        } else {
            v.layout(left, bottom - v.getMeasuredHeight(), right, bottom);
        }
    }

    private boolean isInnerKeyboardVisible() {
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

}
