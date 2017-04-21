package guru.ioio.bravo.jmessage.model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by daniel on 17-4-20.
 */

public class ViewCleaner {
    public static void clean(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup p = (ViewGroup) v;
            for (int i = 0; i < p.getChildCount(); i++) {
                clean(p.getChildAt(i));
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setText(null);
        } else if (v instanceof ImageView) {
            ((ImageView) v).setImageDrawable(null);
        }
    }
}
