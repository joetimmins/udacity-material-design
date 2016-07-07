package com.novoda.materialised.hackernews.asynclistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public final class AsyncListViewPresenter<T extends ViewModel, U extends View & ModelledView<T>> implements AsyncListView<T> {

    private final View loadingView;
    private final RecyclerView topStoriesView;
    private final ModelledViewInflater<U> viewInflater;

    private SingleTypeAdapter<T, U> adapter;

    public AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView, ModelledViewInflater<U> viewInflater) {
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

}
