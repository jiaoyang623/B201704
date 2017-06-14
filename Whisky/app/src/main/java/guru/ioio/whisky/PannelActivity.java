package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.databinding.ActivityPannelBinding;


/**
 * Created by daniel on 6/14/17.
 * for small test
 */

public class PannelActivity extends Activity {
    private ActivityPannelBinding mBinding;

    public CharSequence getContent() {
        String data = getString(R.string.test_too_much);
        SpannableString span = new SpannableString(data);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Utils.toast("click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xffffaa11);
                ds.setUnderlineText(false);
            }
        }, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pannel);
        mBinding.setPresenter(this);
    }
}
