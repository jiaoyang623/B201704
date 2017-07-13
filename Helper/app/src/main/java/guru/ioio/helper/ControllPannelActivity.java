package guru.ioio.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.helper.databinding.ActivityControlPannelBinding;
import guru.ioio.helper.model.AudioHelper;

/**
 * Created by daniel on 7/12/17.
 * for changing settings
 */

public class ControllPannelActivity extends Activity {
    public AudioHelper audioHelper;
    private ActivityControlPannelBinding mBinding;
    private BroadcastReceiver mVolumnChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mBinding != null) {
                mBinding.setPresenter(ControllPannelActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioHelper = new AudioHelper(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_control_pannel);
        registerReceiver(mVolumnChangeReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mVolumnChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.setPresenter(this);
    }
}
