package guru.ioio.bravo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.bravo.databinding.ActivityGifBinding;

/**
 * Created by daniel on 17-5-17.
 */

public class GifActivity extends Activity {
    public String imageUri = "http://s9.rr.itc.cn/r/wapChange/20156_20_7/a76udy4138666869519.gif";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGifBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_gif);
        binding.setPresenter(this);
    }
}
