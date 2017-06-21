package guru.ioio.whisky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import guru.ioio.whisky.base.RVBindingBaseAdapter;
import guru.ioio.whisky.model.RecyclerBean;

/**
 * Created by daniel on 6/19/17.
 * for viewpager using
 */

public class RVViewPagerActivity extends RecyclerActivity {
    @Override
    protected RVBindingBaseAdapter getAdapter() {
        return new RVBindingBaseAdapter<RecyclerBean>(R.layout.item_pager, BR.data)
                .add(getMock(10)).addPresenter(BR.presenter, this);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getApplicationContext(),
                1, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mBinding.recycler);
    }
}
