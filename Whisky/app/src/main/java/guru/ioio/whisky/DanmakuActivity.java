package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.whisky.databinding.ActivityDanmakuBinding;

/**
 * Created by daniel on 6/28/17.
 * test for danmaku
 */

public class DanmakuActivity extends Activity implements DanmakuView.OnEmptyListener {
    private List<DanmakuItem> messageList = new ArrayList<>();
    private ActivityDanmakuBinding mBinding;
    public ObservableField<String> text = new ObservableField<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_danmaku);
        mBinding.setPresenter(this);
        mBinding.danmaku.setOnEmptyListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.danmaku.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.danmaku.show();
    }

    public boolean send() {
        String t = text.get().trim();
        if (!TextUtils.isEmpty(t)) {
            messageList.add(new DanmakuItem(this, t, mBinding.danmaku.getWidth()));
            onEmpty();
            text.set(null);
        }

        return true;
    }

    @Override
    public void onEmpty() {
        if (!messageList.isEmpty()) {
            for (DanmakuItem msg : messageList) {
                mBinding.danmaku.addItem(msg);
            }
        }
    }
}
