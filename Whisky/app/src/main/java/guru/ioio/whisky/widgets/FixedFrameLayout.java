package guru.ioio.whisky.widgets;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by daniel on 3/18/17.
 * 第一次设定后高度固定，宽度全屏的layout
 */

public class FixedFrameLayout extends FrameLayout {

    public FixedFrameLayout(@NonNull Context context) {
        super(context);
    }

    public FixedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int w0 = MeasureSpec.getSize(widthMeasureSpec);
        int h0 = MeasureSpec.getSize(heightMeasureSpec);
        w = Math.max(w, w0);
        h = Math.max(h, h0);
        setMeasuredDimension(w, h);
        measureChildren(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST));
    }

}
