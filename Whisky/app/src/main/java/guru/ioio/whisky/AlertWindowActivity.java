package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.whisky.databinding.ActivityAlertWindowBinding;

/**
 * Created by daniel on 8/1/17.
 */

public class AlertWindowActivity extends Activity {
    private ActivityAlertWindowBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_alert_window);
        mBinding.setPresenter(this);
    }
}
