package guru.ioio.bravo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.bravo.databinding.ActivityPull2refreshBinding;

/**
 * Created by daniel on 6/5/17.
 */

public class Pull2RefreshActivity extends Activity {
    private ActivityPull2refreshBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pull2refresh);
        mBinding.setPresenter(this);
    }
}
