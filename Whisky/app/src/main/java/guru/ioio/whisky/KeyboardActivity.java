package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.databinding.ActivityKeyboardBinding;
import guru.ioio.whisky.widgets.KKLayout;

/**
 * Created by daniel on 6/19/17.
 * for test
 */

public class KeyboardActivity extends Activity implements KKLayout.OnKeyboardChangeListener {
    public String bgUri = "http://img0.pcauto.com.cn/pcauto/1607/04/8409974_405.png";
    private ActivityKeyboardBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_keyboard);
        mBinding.setPresenter(this);
        mBinding.kk.setOnKeyboardChangeListener(this);
    }


    public boolean onEmojiClick() {
        mBinding.kk.showInnerKeyboard(mBinding.edit, mBinding.keyboardLayer.getVisibility() != View.VISIBLE, R.id.keyboard_layer);
        return true;
    }

    @Override
    public void onKeyboardChange(KKLayout.KeyboardStatus status) {
        Utils.j(KeyboardActivity.class, status);
    }
}
