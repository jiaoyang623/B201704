package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.whisky.base.RVBindingBaseAdapter;
import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.databinding.ActivityRecycleBinding;
import guru.ioio.whisky.model.RecyclerBean;

/**
 * Created by jiaoyang on 16/06/2017.
 * for RecyclerView
 */

public class RecyclerActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    protected ActivityRecycleBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recycle);
        mBinding.setPresenter(this);
        mBinding.refresh.setOnRefreshListener(this);

        mBinding.recycler.setLayoutManager(getLayoutManager());
        mBinding.recycler.setAdapter(getAdapter());
    }

    protected RVBindingBaseAdapter getAdapter() {
        return new RVBindingBaseAdapter<RecyclerBean>(R.layout.item_recycler, BR.data)
                .add(getMock()).addPresenter(BR.presenter, this);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getApplicationContext(),
                2, GridLayoutManager.VERTICAL, false);
    }

    protected List<RecyclerBean> getMock() {
        List<RecyclerBean> list = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            RecyclerBean bean = new RecyclerBean("title " + i, "desc", "time" + i);
            switch (i % 3) {
                case 0:
                    bean.desc = "small";
                    break;
                case 1:
                    bean.desc = "mAdapter = new RVBindingBaseAdapter<RecyclerBean>(R.layout.item_recycler, BR.data).add(getMock())";
                    break;
                case 2:
                    bean.desc = "mBinding.recycler.setLayoutManager(getLayoutManager())";
                    break;
            }
            list.add(bean);
        }
        return list;
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.refresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public boolean onItemClick(RecyclerBean bean) {
        Utils.toast(bean.title);
        return true;
    }
}
