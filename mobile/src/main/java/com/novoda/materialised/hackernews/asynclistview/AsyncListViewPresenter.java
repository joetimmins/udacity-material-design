package com.novoda.materialised.hackernews.asynclistview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.novoda.materialised.R;

import java.util.List;

public final class AsyncListViewPresenter<T extends ViewData<Integer>,
        V extends View & ModelledView<T>>
        implements AsyncListView<T> {

    private final View loadingView;
    private final RecyclerView topStoriesView;
    private final ModelledViewInflater<V> viewInflater;

    private SingleTypeAdapter<T, V> adapter;

    public AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView, Class<V> viewClass) {
        this(loadingView, topStoriesView, new ModelledViewInflater<>(viewClass));
    }

    private AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView, ModelledViewInflater<V> viewInflater) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
        this.viewInflater = viewInflater;
    }

    @Override
    public void updateWith(@NonNull List<ViewModel<T>> initialViewModelList) {
        TextView textView = (TextView) loadingView;
        textView.setText(textView.getContext().getResources().getString(R.string.loading_stories));
        loadingView.setVisibility(View.VISIBLE);

        adapter = new SingleTypeAdapter<>(initialViewModelList, viewInflater);
        topStoriesView.swapAdapter(adapter, false);
    }

    @Override
    public void updateWith(@NonNull ViewModel<T> viewModel) {
        loadingView.setVisibility(View.GONE);
        adapter.updateWith(viewModel);
    }

    @Override
    public void showError() {
        TextView textView = (TextView) loadingView;
        textView.setText(R.string.bork_bork);
        loadingView.setVisibility(View.VISIBLE);
    }

}
