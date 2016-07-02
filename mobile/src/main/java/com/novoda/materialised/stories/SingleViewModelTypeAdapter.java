package com.novoda.materialised.stories;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.hackernews.ClickListener;
import com.novoda.materialised.hackernews.ViewModel;
import com.novoda.materialised.hackernews.ViewModelWithClickListener;

import java.util.List;

import static com.novoda.materialised.hackernews.ClickListenerBuilderKt.addNoOpClickListeners;

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
        ViewModelWithClickListener<T> data = viewModelWithClickListeners.get(position);
        view.updateWith(data.getViewModel(), data.getClickListener());
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
                viewModelWithClickListeners.set(positionToUpdate, new ViewModelWithClickListener<>(newItem, clickListener));
                notifyItemChanged(positionToUpdate);
                break;
            }
        }
    }

    public interface UpdatableView<U extends ViewModel> {
        void updateWith(U data, ClickListener<U> clickListener);
    }
}
