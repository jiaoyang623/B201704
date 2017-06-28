package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.databinding.ActivityDanmakuBinding;

/**
 * Created by daniel on 6/28/17.
 * test for danmaku
 */

public class DanmakuActivity extends Activity {
    private ActivityDanmakuBinding mBinding;
    public ObservableField<String> text = new ObservableField<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_danmaku);
        mBinding.setPresenter(this);
    }

    public boolean send() {
        Utils.toast(text.get());
        text.set(null);

        return true;
    }
}
