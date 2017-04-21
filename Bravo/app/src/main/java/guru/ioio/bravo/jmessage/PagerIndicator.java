package guru.ioio.bravo.jmessage;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by daniel on 17-4-19.
 */

public class PagerIndicator extends View {
    private int mCount = 4, mSelection = 1;
    private int mHeight = 0, mWidth = 0;
    private int mColor = 0xffbbbbbb, mSelectColor = 0xff999999;
    private Paint mPaint;

    public PagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = bottom - top;
        mWidth = right - left;
    }

    public void setColor(int color) {
        mCount = color;
    }

    public void setSelectColor(int color) {
        mSelectColor = color;
    }

    public void setCount(int i) {
        mCount = Math.max(i, 0);
    }

    public void setSelection(int i) {
        mSelection = Math.max(0, Math.min(mCount - 1, i));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCount <= 0) {
            return;
        } else if (mCount == 1) {
            mPaint.setColor(mSelectColor);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mHeight / 2, mPaint);
        } else {
            for (int i = 0; i < mCount; i++) {
                int color = (i == mSelection) ? mSelectColor : mColor;
                mPaint.setColor(color);
                float r = mHeight / 2;
                float y = r;
                float x = (mWidth - mHeight) / (mCount - 1) * i + r;
                canvas.drawCircle(x, y, r, mPaint);
            }
        }
    }
}
