package com.novoda.materialised.hackernews.asynclistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public final class AsyncListViewPresenter<T extends ViewModel, V extends View & ModelledView<T>> implements AsyncListView<T> {

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
    public void updateWith(List<T> initialViewModelList) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleTypeAdapter<>(initialViewModelList, viewInflater);
        topStoriesView.swapAdapter(adapter, false);
    }

    @Override
    public void updateWith(T viewModel) {
        adapter.updateWith(viewModel);
    }

    @Override
    public void showError() {

    }

}
