package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public final class SingleTypeAdapter<T> extends RecyclerView.Adapter {
    @LayoutRes
    private final int layoutRes;
    private final List<T> viewModels;

    public SingleTypeAdapter(List<T> viewModels, int layoutRes) {
        this.viewModels = viewModels;
        this.layoutRes = layoutRes;
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
        view.updateWith(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public interface UpdatableView<T> {
        void updateWith(T data);
    }
}
