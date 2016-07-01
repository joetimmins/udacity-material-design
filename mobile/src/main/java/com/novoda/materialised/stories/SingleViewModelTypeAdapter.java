package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.hackernews.ClickListener;
import com.novoda.materialised.hackernews.NoOpClickListener;
import com.novoda.materialised.hackernews.ViewModel;

import java.util.ArrayList;
import java.util.List;

public final class SingleViewModelTypeAdapter<T extends ViewModel> extends RecyclerView.Adapter {
    @LayoutRes
    private final int layoutRes;
    private final List<ViewModelWithClickListener<T>> viewModelWithClickListeners;

    public SingleViewModelTypeAdapter(List<T> viewModels, int layoutRes) {
        this.viewModelWithClickListeners = addNoOpClickListeners(viewModels);
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
        view.updateWith(
                viewModelWithClickListeners.get(position).getViewModel(),
                viewModelWithClickListeners.get(position).getClickListener()
        );
    }

    @Override
    public int getItemCount() {
        return viewModelWithClickListeners.size();
    }

    @Override
    public long getItemId(int position) {
        return viewModelWithClickListeners.get(position).getViewModel().getId();
    }

    public void updateWith(T newItem, ClickListener<T> clickListener) {
        for (ViewModelWithClickListener<T> viewModel : viewModelWithClickListeners) {
            if (viewModel.getViewModel().getId() == newItem.getId()) {
                int positionToUpdate = viewModelWithClickListeners.indexOf(viewModel);
                viewModelWithClickListeners.set(positionToUpdate, addClickListener(newItem, clickListener));
                notifyItemChanged(positionToUpdate);
                break;
            }
        }
    }

    private List<ViewModelWithClickListener<T>> addNoOpClickListeners(List<T> viewModels) {
        List<ViewModelWithClickListener<T>> result = new ArrayList<>();
        for (T viewModel : viewModels) {
            result.add(addClickListener(viewModel, new NoOpClickListener<T>()));
        }
        return result;
    }

    private ViewModelWithClickListener<T> addClickListener(T viewModel, ClickListener<T> clickListener) {
        return new ViewModelWithClickListener<>(viewModel, clickListener);
    }

    public interface UpdatableView<U extends ViewModel> {
        void updateWith(U data, ClickListener<U> clickListener);
    }

    public static class ViewModelWithClickListener<V extends ViewModel> {
        private final V viewModel;
        private final ClickListener<V> clickListener;

        public ViewModelWithClickListener(V viewModel, ClickListener<V> clickListener) {
            this.viewModel = viewModel;
            this.clickListener = clickListener;
        }

        public V getViewModel() {
            return viewModel;
        }

        public ClickListener<V> getClickListener() {
            return clickListener;
        }
    }
}
