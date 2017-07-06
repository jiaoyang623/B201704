package guru.ioio.whisky.widgets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by daniel on 7/6/17.
 */

public class RoundFillDrawable extends Drawable {
    private int color = 0;
    private int corner = 0;
    private Paint paint;
    private Path path;

    public RoundFillDrawable() {
        this(0, 0);
    }

    public RoundFillDrawable(int color, int cornor) {
        this.color = color;
        this.corner = cornor;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        path = new Path();
    }

    public RoundFillDrawable setColor(int color) {
        this.color = color;
        paint.setColor(color);
        return this;
    }

    public RoundFillDrawable setCorner(int size) {
        this.corner = size;
        return this;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        path.reset();
        path.addRoundRect(new RectF(bounds), corner, corner, Path.Direction.CW);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
