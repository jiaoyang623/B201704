package guru.ioio.whisky.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaoyang on 16/06/2017.
 * base adapter for RecyclerView.Adapter
 */

public class RVBindingBaseAdapter<T> extends RecyclerView.Adapter<RVBindingBaseAdapter.RViewHolder> {
    private final int sLayoutId, sValueId;
    public List<T> mList = new ArrayList<>();
    private Map<Integer, Object> mPresenterMap = new HashMap<>();

    public RVBindingBaseAdapter<T> set(List<T> data) {
        mList.clear();
        if (data != null) {
            mList.addAll(data);
            notifyItemRangeChanged(0, mList.size());
        }

        return this;
    }

    public RVBindingBaseAdapter<T> add(List<T> data) {
        if (data == null) {
            return this;
        }

        data.removeAll(mList);
        if (data.size() > 0) {
            int t = mList.size();
            mList.addAll(data);
            notifyItemRangeInserted(t, data.size());
        }

        return this;
    }

    public RVBindingBaseAdapter<T> addPresenter(int variableId, Object presenter) {
        mPresenterMap.put(variableId, presenter);
        return this;
    }

    public RVBindingBaseAdapter<T> removePresenter(int variableId) {
        if (mPresenterMap.containsKey(variableId)) {
            mPresenterMap.remove(variableId);
        }

        return this;
    }

    public RVBindingBaseAdapter(int layoutId, int valueId) {
        sLayoutId = layoutId;
        sValueId = valueId;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), sLayoutId, parent, false);
        return new RViewHolder(binding, sValueId, mPresenterMap);
    }

    public void onBindViewHolder(RViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class RViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private int valueId;
        private Map<Integer, Object> presenterMap;

        public RViewHolder(ViewDataBinding binding, int valueId, Map<Integer, Object> presenterMap) {
            super(binding.getRoot());
            this.binding = binding;
            this.valueId = valueId;
            this.presenterMap = presenterMap;
        }

        public void bind(Object t) {
            binding.setVariable(valueId, t);
            if (presenterMap != null && presenterMap.size() > 0) {
                for (Map.Entry<Integer, Object> entry : presenterMap.entrySet()) {
                    binding.setVariable(entry.getKey(), entry.getValue());
                }
            }
            binding.executePendingBindings();
        }
    }
}
