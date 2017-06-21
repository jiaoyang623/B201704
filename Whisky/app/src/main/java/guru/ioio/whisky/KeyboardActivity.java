package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import guru.ioio.whisky.databinding.ActivityKeyboardBinding;

/**
 * Created by daniel on 6/19/17.
 * for test
 */

public class KeyboardActivity extends Activity {
    public String bgUri = "http://img0.pcauto.com.cn/pcauto/1607/04/8409974_405.png";
    private ActivityKeyboardBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_keyboard);
        mBinding.setPresenter(this);
    }


    public boolean onEmojiClick() {
        mBinding.keyboardLayer.setVisibility(mBinding.keyboardLayer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        return true;
    }
}
