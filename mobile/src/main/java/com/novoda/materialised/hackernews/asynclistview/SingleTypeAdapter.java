package com.novoda.materialised.hackernews.asynclistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

final class SingleTypeAdapter<T extends ViewModel, V extends View & ModelledView<T>> extends RecyclerView.Adapter<ModelledViewHolder<V>> {
    private final List<T> viewModels;
    private final ModelledViewInflater<V> viewInflater;

    SingleTypeAdapter(List<T> viewModels, ModelledViewInflater<V> viewInflater) {
        this.viewModels = viewModels;
        this.viewInflater = viewInflater;
        setHasStableIds(true);
    }

    @Override
    public ModelledViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V view = viewInflater.inflateUsing(parent);
        return new ModelledViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(ModelledViewHolder<V> holder, int position) {
        V view = holder.obtainHeldView();
        T viewModel = viewModels.get(position);
        view.updateWith(viewModel);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public long getItemId(int position) {
        return viewModels.get(position).getViewData().getId();
    }

    void updateWith(T newItem) {
        for (int i = 0; i < viewModels.size(); i++) {
            if (shouldUpdate(i, newItem)) {
                viewModels.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    private boolean shouldUpdate(int position, T newItem) {
        return viewModels.get(position).getViewData().getId() == newItem.getViewData().getId();
    }
}
