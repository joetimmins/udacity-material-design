package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.hackernews.ClickListener;
import com.novoda.materialised.hackernews.ViewModel;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public final class SingleViewModelTypeAdapter<T extends ViewModel> extends RecyclerView.Adapter {
    @LayoutRes
    private final int layoutRes;
    private final List<T> viewModels;
    private final List<ClickListener<T>> clickListeners;

    public SingleViewModelTypeAdapter(List<T> viewModels, int layoutRes) {
        this.viewModels = viewModels;
        this.layoutRes = layoutRes;
        clickListeners = buildNullSafeClickListeners(viewModels.size());
        setHasStableIds(true);
    }

    @NonNull
    private List<ClickListener<T>> buildNullSafeClickListeners(int size) {
        List<ClickListener<T>> clickListeners = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            clickListeners.add(i, new NoOpClickListener());
        }
        return clickListeners;
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
        view.updateWith(viewModels.get(position), clickListeners.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public long getItemId(int position) {
        return viewModels.get(position).getId();
    }

    public void updateWith(T newItem, ClickListener<T> clickListener) {
        for (T viewModel : viewModels) {
            if (viewModel.getId() == newItem.getId()) {
                int positionToUpdate = viewModels.indexOf(viewModel);
                viewModels.set(positionToUpdate, newItem);
                clickListeners.set(positionToUpdate, clickListener);
                notifyItemChanged(positionToUpdate);
                break;
            }
        }
    }

    public interface UpdatableView<U extends ViewModel> {
        void updateWith(U data, ClickListener<U> clickListener);
    }

    private class NoOpClickListener implements ClickListener<T> {
        @Override
        public void onClick(@NotNull T viewModel) {
            // No-op
        }
    }
}
