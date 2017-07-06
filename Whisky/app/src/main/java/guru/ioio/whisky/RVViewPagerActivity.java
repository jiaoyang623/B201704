package guru.ioio.whisky;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import guru.ioio.whisky.base.RVBindingBaseAdapter;
import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.model.RecyclerBean;

/**
 * Created by daniel on 6/19/17.
 * for viewpager using
 */

public class RVViewPagerActivity extends RecyclerActivity {
    private RVBindingBaseAdapter<RecyclerBean> mAdapter;

    @Override
    protected RVBindingBaseAdapter getAdapter() {
        mAdapter = new RVBindingBaseAdapter<RecyclerBean>(R.layout.item_pager, BR.data)
                .add(getMock(10)).addPresenter(BR.presenter, this);
        return mAdapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getApplicationContext(),
                1, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mBinding.recycler);
        mBinding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Utils.j(RVViewPagerActivity.class, "onScrollStateChanged",
                                mBinding.recycler.getChildAdapterPosition(helper.findSnapView(mBinding.recycler.getLayoutManager())));
                        break;
                }
            }
        });

        mBinding.recycler.setItemAnimator(new RecyclerView.ItemAnimator() {
            @Override
            public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public void runPendingAnimations() {
            }

            @Override
            public void endAnimation(RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
    }

    @Override
    public boolean onItemClick(RecyclerBean bean) {
        mAdapter.add(getMock(10));
        return super.onItemClick(bean);
    }
}
