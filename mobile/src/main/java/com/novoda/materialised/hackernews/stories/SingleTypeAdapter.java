package com.novoda.materialised.hackernews.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.hackernews.generics.ViewModel;

import java.util.List;

public final class SingleTypeAdapter<T extends ViewModel> extends RecyclerView.Adapter {
    @LayoutRes
    private final int layoutRes;
    private final List<T> viewModels;

    public SingleTypeAdapter(List<T> viewModels, int layoutRes) {
        this.viewModels = viewModels;
        this.layoutRes = layoutRes;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UpdatableView<T> view = (UpdatableView<T>) holder.itemView;
        T data = viewModels.get(position);
        view.updateWith(data);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public long getItemId(int position) {
        return viewModels.get(position).getViewData().getId();
    }

    public void updateWith(T newItem) {
        for (int i = 0; i < viewModels.size(); i++) {
            if (shouldUpdate(i, newItem)) {
                viewModels.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    private boolean shouldUpdate(int i, T newItem) {
        return viewModels.get(i).getViewData().getId() == newItem.getViewData().getId();
    }

    public interface UpdatableView<U extends ViewModel> {
        void updateWith(U viewModel);
    }
}
