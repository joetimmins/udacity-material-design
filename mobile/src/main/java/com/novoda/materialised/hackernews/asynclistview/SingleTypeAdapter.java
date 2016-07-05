package com.novoda.materialised.hackernews.asynclistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public final class SingleTypeAdapter<T extends ViewModel, U extends View & UpdatableView<T>> extends RecyclerView.Adapter {
    private final List<T> viewModels;
    private final UpdatableViewInflater<U> viewInflater;

    public SingleTypeAdapter(List<T> viewModels, UpdatableViewInflater<U> viewInflater) {
        this.viewModels = viewModels;
        this.viewInflater = viewInflater;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewInflater.inflateUpdatableView(parent);
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
}
