package guru.ioio.bravo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-27.
 * for padding research
 */

public class FixedPaddingRelativeLayout extends RelativeLayout {
    public FixedPaddingRelativeLayout(Context context) {
        super(context);
    }

    public FixedPaddingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedPaddingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FixedPaddingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Utils.j("onLayout", l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Utils.j("onMeasure", MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }
}
