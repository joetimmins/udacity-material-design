package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public final class SingleTypeAdapter<T, U> extends RecyclerView.Adapter {
    private final List<T> storyViewModels;
    private final Class<U> viewClass;
    private final ViewDataBinder<T, U> viewDataBinder;

    @LayoutRes
    private final int layoutRes;

    public SingleTypeAdapter(List<T> storyViewModels, Class<U> viewClass, ViewDataBinder<T, U> viewDataBinder, int layoutRes) {
        this.storyViewModels = storyViewModels;
        this.viewClass = viewClass;
        this.viewDataBinder = viewDataBinder;
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
//        viewDataBinder.bind(storyViewModels.get(position), viewClass.cast(holder.itemView));
        new DefaultViewDataBinder<T>().bind(storyViewModels.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return storyViewModels.size();
    }

    public interface ViewDataBinder<T, U> {
        void bind(T data, U viewInstance);
    }

    public static class DefaultViewDataBinder<T> {
        public void bind(T data, RecyclerView.ViewHolder holder) {
            UpdatableView<T> view = (UpdatableView<T>) holder.itemView;
            view.updateWith(data);
        }
    }

    public interface UpdatableView<T> {
        void updateWith(T data);
    }
}
