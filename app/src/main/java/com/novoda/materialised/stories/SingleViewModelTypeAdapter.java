package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.hackernews.ViewModel;

import java.util.List;

public final class SingleViewModelTypeAdapter<T extends ViewModel> extends RecyclerView.Adapter {
    @LayoutRes
    private final int layoutRes;
    private final List<T> viewModels;

    public SingleViewModelTypeAdapter(List<T> viewModels, int layoutRes) {
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
        view.updateWith(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public long getItemId(int position) {
        return viewModels.get(position).getId();
    }

    public void updateWith(T newItem) {
        for (T viewModel : viewModels) {
            if (viewModel.getId() == newItem.getId()) {
                int indexToUpdate = viewModels.indexOf(viewModel);
                viewModels.set(indexToUpdate, newItem);
                notifyItemChanged(indexToUpdate);
                break;
            }
        }
    }

    public interface UpdatableView<U extends ViewModel> {
        void updateWith(U data);
    }
}
