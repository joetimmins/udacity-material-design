package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.novoda.materialised.hackernews.generics.AsyncListView;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;
import com.novoda.materialised.stories.SingleViewModelTypeAdapter;

import java.util.List;

final class TopStoriesViewPresenter implements AsyncListView<StoryViewModel> {

    private final View loadingView;
    private final RecyclerView topStoriesView;

    private SingleViewModelTypeAdapter<StoryViewModel> adapter;

    TopStoriesViewPresenter(View loadingView, RecyclerView topStoriesView) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
    }

    @Override
    public void updateWith(List<StoryViewModel> initialViewModelList) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleViewModelTypeAdapter<>(initialViewModelList, R.layout.inflatable_story_card);
        topStoriesView.swapAdapter(adapter, false);
    }

    @Override
    public void updateWith(StoryViewModel viewModel) {
        adapter.updateWith(viewModel);
    }
}
