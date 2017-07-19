package guru.ioio.whisky.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 7/18/17.
 */

public class TagView extends View {
    private List<String> mTags = new ArrayList<>();
    private int mColor, mSelectedColor;
    private float mFontSize, mSelectedFontSize;
    private Paint mPaint;
    private int mSelection = 0;
    private float mTagSpace = 10;
    private boolean mNeedMeasure = true;
    private float[] mTagStart = new float[0];
    private float[] mTagEnd = new float[0];

    public TagView(Context context) {
        super(context);
        init(context);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mNeedMeasure = true;
    }

    private void init(Context context) {
        mColor = mSelectedColor = 0xffffffff;
        mFontSize = mSelectedFontSize = 15;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public TagView setTags(List<String> tags) {
        mTags.clear();
        if (tags != null) {
            mTags.addAll(tags);
        }

        mNeedMeasure = true;
        invalidate();

        return this;
    }

    private String mDivider = "";

    public void setTagDivider(String divider) {
        mDivider = divider;
    }

    public void setTags(String tags) {
        mNeedMeasure = true;
        if (TextUtils.isEmpty(tags) || TextUtils.isEmpty(mDivider)) {
            mTags.clear();
        }

        String[] array = tags.split(mDivider);
        mTags.clear();
        for (String t : array) {
            mTags.add(t);
        }
    }


    public void setTagColor(int color) {
        mColor = color;
        invalidate();
    }

    public void setSelectedTagColor(int color) {
        mSelectedColor = color;
        invalidate();
    }

    public void setTagSize(float size) {
        mFontSize = size;
        mNeedMeasure = true;
        invalidate();
    }

    public void setSelectedTagSize(float size) {
        mSelectedFontSize = size;
        mNeedMeasure = true;
        invalidate();
    }

    public void setSelection(int selection) {
        mSelection = selection;
        mNeedMeasure = true;
        invalidate();
    }

    public void setTagSpace(float space) {
        mTagSpace = space;
        mNeedMeasure = true;
        invalidate();
    }

    private float mLineWidth = 10;

    public void setLineWidth(float width) {
        mLineWidth = width;
    }

    private int mLineColor = 0xffffffff;

    public void setLineColor(int color) {
        mLineColor = color;
    }

    private float mLinePadding = 0;

    public void setLinePadding(float padding) {
        mLinePadding = padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTags.size() == 0) {
            return;
        }

        if (mNeedMeasure) {
            measureText();
            mNeedMeasure = false;
        }

        for (int i = 0; i < mTags.size(); i++) {
            if (i == mSelection) {
                mPaint.setTextSize(mSelectedFontSize);
                mPaint.setColor(mSelectedColor);
                mPaint.setFakeBoldText(true);
            } else {
                mPaint.setTextSize(mFontSize);
                mPaint.setColor(mColor);
                mPaint.setFakeBoldText(false);
            }
            canvas.drawText(mTags.get(i), mTagStart[i], getHeight(), mPaint);
        }

        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        float y = mLineWidth / 2;
        canvas.drawLine(mTagStart[mSelection] + mLinePadding, y, mTagEnd[mSelection] - mLinePadding, y, mPaint);
    }

    private float sum(float[] array) {
        if (array != null && array.length > 0) {
            float sum = array[0];
            for (int i = 1; i < array.length; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            return 0;
        }
    }


    private void measureText() {
        if (mTags.size() != mTagStart.length) {
            mTagStart = new float[mTags.size()];
            mTagEnd = new float[mTags.size()];
        }

        float[] array = new float[mTags.size()];
        for (int i = 0; i < array.length; i++) {
            if (i == mSelection) {
                mPaint.setTextSize(mSelectedFontSize);
            } else {
                mPaint.setTextSize(mFontSize);
            }
            array[i] = mPaint.measureText(mTags.get(i));
        }

        float start = (getWidth() - sum(array) - mTagSpace * (array.length - 1)) / 2;
        for (int i = 0; i < array.length; i++) {
            mTagStart[i] = start;
            mTagEnd[i] = start + array[i];
            start += array[i] + mTagSpace;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            for (int i = 0; i < mTagStart.length; i++) {
                if (x > mTagStart[i] && x < mTagEnd[i]) {
                    setSelection(i);
                    if (mOnSelectListener != null) {
                        mOnSelectListener.onSelected(i);
                    }
                    break;
                }
            }
            return true;
        } else {
            super.onTouchEvent(event);
            return true;
        }
    }

    private OnSelectListener mOnSelectListener = null;

    public void setOnSelectListener(OnSelectListener l) {
        mOnSelectListener = l;
    }

    public interface OnSelectListener {
        void onSelected(int position);
    }
}
