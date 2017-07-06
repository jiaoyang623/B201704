package guru.ioio.whisky.base;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import guru.ioio.whisky.widgets.RoundFillDrawable;

/**
 * Created by daniel on 17-4-13.
 * custome xml params
 */


public class BindingUtils {
    @BindingAdapter({"android:imageUri"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"android:imageUri", "android:imageDrawable"})
    public static void loadImage(ImageView view, String url, Drawable defaultImage) {
        if (TextUtils.isEmpty(url)) {
            view.setImageDrawable(defaultImage);
        } else {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    @BindingAdapter({"roundRectColor", "roundRectRadius"})
    public static void setBackgoundRect(View view, int color, float radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(new RoundFillDrawable(color, (int) radius));
        } else {
            view.setBackgroundDrawable(new RoundFillDrawable(color, (int) radius));
        }
    }
}
