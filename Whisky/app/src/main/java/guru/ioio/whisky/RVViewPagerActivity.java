package guru.ioio.whisky;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import guru.ioio.whisky.base.RVBindingBaseAdapter;
import guru.ioio.whisky.model.RecyclerBean;

/**
 * Created by daniel on 6/19/17.
 */

public class RVViewPagerActivity extends RecyclerActivity {
    @Override
    protected RVBindingBaseAdapter getAdapter() {
        return new RVBindingBaseAdapter<RecyclerBean>(R.layout.item_pager, BR.data)
                .add(getMock()).addPresenter(BR.presenter, this);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getApplicationContext(),
                1, GridLayoutManager.VERTICAL, false);
    }
}
