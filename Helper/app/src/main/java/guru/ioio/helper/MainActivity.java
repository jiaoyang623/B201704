package guru.ioio.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.helper.base.RVBindingBaseAdapter;
import guru.ioio.helper.databinding.ActivityMainBinding;


public class MainActivity extends Activity {
    public ObservableBoolean hasContent = new ObservableBoolean(false);
    private RVBindingBaseAdapter<ActivityInfo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_activity, BR.data);
        mAdapter.add(getActivities());
        mAdapter.addPresenter(BR.presenter, this);
        hasContent.set(mAdapter.getItemCount() != 0);
        binding.setPresenter(this);
        binding.recycler.setAdapter(mAdapter);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private List<ActivityInfo> getActivities() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            List<ActivityInfo> list = new ArrayList<>(packageInfo.activities.length);
            for (ActivityInfo info : packageInfo.activities) {
                if (MainActivity.class.getName().equals(info.name)) {
                    continue;
                }
                list.add(info);
            }
            return list;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean onItemClick(ActivityInfo info) {
        Intent intent = new Intent();
        intent.setClassName(getApplicationContext(), info.name);
        startActivity(intent);

        return true;
    }
}
