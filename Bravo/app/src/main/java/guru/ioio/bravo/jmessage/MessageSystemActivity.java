package guru.ioio.bravo.jmessage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.bravo.R;
import guru.ioio.bravo.base.BindingBaseAdapter;
import guru.ioio.bravo.jmessage.model.SystemBean;

/**
 * Created by daniel on 17-4-20.
 * for system notification
 */

public class MessageSystemActivity extends MessageBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        title = "评论";
        adapter = new BindingBaseAdapter<>(R.layout.item_system, BR.data);
        super.onCreate(savedInstanceState);

        adapter.setData(getMock());
    }

    public List<SystemBean> getMock() {
        List<SystemBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SystemBean bean = new SystemBean();
            bean.description = "hahasdfwe";
            bean.imageUri = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png";
            bean.time = System.currentTimeMillis();
            bean.showDate = i % 2 == 0;
            list.add(bean);
        }
        return list;
    }
}
