package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import guru.ioio.whisky.base.RBindingBaseAdapter;
import guru.ioio.whisky.databinding.ActivityRecycleBinding;
import guru.ioio.whisky.model.RecyclerBean;

/**
 * Created by jiaoyang on 16/06/2017.
 * for RecyclerView
 */

public class RecyclerActivity extends Activity {
    private ActivityRecycleBinding mBinding;
    private RBindingBaseAdapter<RecyclerBean> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recycle);
        mBinding.setPresenter(this);
        mAdapter = new RBindingBaseAdapter<RecyclerBean>(R.layout.item_recycler, BR.data)
                .add(getMock());
        mBinding.recycler.setAdapter(mAdapter);
        GridLayoutManager mgr = new GridLayoutManager(getApplicationContext(),
                GridLayoutManager.DEFAULT_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mBinding.recycler.setLayoutManager(mgr);
    }

    private List<RecyclerBean> getMock() {
        List<RecyclerBean> list = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            RecyclerBean bean = new RecyclerBean("title " + i, "desc", new Date().toString());
            list.add(bean);
        }
        return list;
    }

}
