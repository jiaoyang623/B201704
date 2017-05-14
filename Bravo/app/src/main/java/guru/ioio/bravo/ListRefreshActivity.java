package guru.ioio.bravo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.bravo.base.BindingBaseAdapter;
import guru.ioio.bravo.databinding.ActivityListRefreshBinding;

/**
 * Created by daniel on 17-5-14.
 */

public class ListRefreshActivity extends Activity {
    public BindingBaseAdapter<ListRefreshBean> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BindingBaseAdapter<>(R.layout.item_list_refresh, BR.data);
        ActivityListRefreshBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_list_refresh);
        binding.setPresenter(this);

        List<ListRefreshBean> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add(new ListRefreshBean());
        }
        adapter.setData(list);
    }


    public static class ListRefreshBean {
        public ObservableInt state = new ObservableInt(0);

        public boolean onClick() {
            state.set(state.get() == 0 ? 1 : 0);
            return true;
        }
    }
}
