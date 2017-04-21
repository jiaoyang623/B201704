package guru.ioio.bravo.base;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by daniel on 17-4-13.
 * custome xml params
 */


public class BindingUtils {
    @BindingAdapter({"android:imageUri"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).dontAnimate().into(view);
    }

    @BindingAdapter({"android:imageUri", "android:imageDrawable"})
    public static void loadImage(ImageView view, String url, Drawable defaultImage) {
        if (TextUtils.isEmpty(url)) {
            view.setImageDrawable(defaultImage);
        } else {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }
}
