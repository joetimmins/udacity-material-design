package com.novoda.materialised.hackernews;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.novoda.materialised.R;
import com.novoda.materialised.hackernews.generics.AsyncListView;
import com.novoda.materialised.hackernews.generics.ViewModel;
import com.novoda.materialised.hackernews.stories.SingleTypeAdapter;

import java.util.List;

final class AsyncListViewPresenter<T extends ViewModel> implements AsyncListView<T> {

    private final View loadingView;
    private final RecyclerView topStoriesView;

    private SingleTypeAdapter<T> adapter;

    AsyncListViewPresenter(View loadingView, RecyclerView topStoriesView) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
    }

    @Override
    public void updateWith(List<T> initialViewModelList) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleTypeAdapter<>(initialViewModelList, R.layout.inflatable_story_card);
        topStoriesView.swapAdapter(adapter, false);
    }

    @Override
    public void updateWith(T viewModel) {
        adapter.updateWith(viewModel);
    }
}
