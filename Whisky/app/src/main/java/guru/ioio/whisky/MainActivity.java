package guru.ioio.whisky;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.whisky.base.BindingBaseAdapter;
import guru.ioio.whisky.databinding.ActivityMainBinding;


public class MainActivity extends Activity {
    public BindingBaseAdapter<ActivityInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new BindingBaseAdapter<>(R.layout.item_activity, BR.data);
        adapter.setData(getActivities());
        adapter.addPresenter(BR.presenter, this);
        binding.setPresenter(this);
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
