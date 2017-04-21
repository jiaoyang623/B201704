package guru.ioio.bravo.jmessage;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.bravo.R;
import guru.ioio.bravo.base.BindingBaseAdapter;
import guru.ioio.bravo.databinding.ActivityBaseMessageBinding;

/**
 * Created by daniel on 17-4-21.
 * base activity for message_system and message_comment
 */

public class MessageBaseActivity extends Activity {
    public String title;
    public BindingBaseAdapter adapter;
    protected ActivityBaseMessageBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_message);
        mBinding.setPresenter(this);
    }

    public boolean onBackClick() {
        finish();
        return true;
    }
}
