package guru.ioio.helper;

import android.app.Activity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioHelper = new AudioHelper(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_control_pannel);
        mBinding.setPresenter(this);
    }


}
