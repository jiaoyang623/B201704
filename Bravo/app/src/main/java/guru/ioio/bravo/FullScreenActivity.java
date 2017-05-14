package guru.ioio.bravo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import guru.ioio.bravo.databinding.ActivityFullscreenBinding;

/**
 * Created by daniel on 17-4-27.
 * test for full screen with transparent status bar
 */

public class FullScreenActivity extends Activity {
    public String background = "http://cn.bing.com/az/hprichbg/rb/CivitadiBagnoregio_ZH-CN12942138675_1920x1080.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFullscreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fullscreen);
        binding.setPresenter(this);
        binding.getRoot().setPadding(5, 5, 5, 5);
    }
}
