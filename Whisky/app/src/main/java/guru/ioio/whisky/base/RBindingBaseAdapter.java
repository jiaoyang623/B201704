package guru.ioio.whisky.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaoyang on 16/06/2017.
 * base adapter for RecyclerView.Adapter
 */

public class RBindingBaseAdapter<T> extends RecyclerView.Adapter<RBindingBaseAdapter.RViewHolder> {
    private final int sLayoutId, sValueId;
    public List<T> mList = new ArrayList<>();

    public RBindingBaseAdapter<T> set(List<T> data) {
        mList.clear();
        if (data != null) {
            mList.addAll(data);
            notifyItemRangeChanged(0, mList.size());
        }

        return this;
    }

    public RBindingBaseAdapter<T> add(List<T> data) {
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

    public RBindingBaseAdapter(int layoutId, int valueId) {
        sLayoutId = layoutId;
        sValueId = valueId;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), sLayoutId, parent, false);
        return new RViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public RViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(T t) {
            binding.setVariable(sValueId, t);
            binding.executePendingBindings();
        }
    }
}
