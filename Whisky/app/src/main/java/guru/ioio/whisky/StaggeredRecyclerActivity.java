package guru.ioio.whisky;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by daniel on 6/19/17.
 * for custom LayoutManager
 */

public class StaggeredRecyclerActivity extends RecyclerActivity {
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
