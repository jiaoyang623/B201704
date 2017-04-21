package guru.ioio.bravo.jmessage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-20.
 */

public class ChatImageView extends ImageView {
    private int imageWidth;
    private int imageHeight;

    public ChatImageView(Context context) {
        super(context);
    }

    public ChatImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (imageWidth == 0 || imageHeight == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int h, w = MeasureSpec.getSize(widthMeasureSpec);
            if (imageWidth < w) {
                w = imageWidth;
                h = imageHeight;
            } else {
                h = imageHeight * w / imageWidth;
            }
            Utils.j("ChatImageView", imageWidth, imageHeight, MeasureSpec.getSize(widthMeasureSpec));
            setMeasuredDimension(w, h);
        }
    }
}
