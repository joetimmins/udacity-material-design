package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.novoda.materialised.hackernews.generics.DecoupledAsyncListView;
import com.novoda.materialised.hackernews.topstories.DecoupledStoryViewModel;
import com.novoda.materialised.stories.SingleViewModelTypeAdapter;

import java.util.List;

final class TopStoriesViewPresenter implements DecoupledAsyncListView<DecoupledStoryViewModel> {

    private final View loadingView;
    private final RecyclerView topStoriesView;

    private SingleViewModelTypeAdapter<DecoupledStoryViewModel> adapter;

    TopStoriesViewPresenter(View loadingView, RecyclerView topStoriesView) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
    }

    @Override
    public void updateWith(List<DecoupledStoryViewModel> initialViewModelList) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleViewModelTypeAdapter<>(initialViewModelList, R.layout.inflatable_story_card);
        topStoriesView.swapAdapter(adapter, false);
    }

    @Override
    public void updateWith(DecoupledStoryViewModel viewModel) {
        adapter.updateWith(viewModel);
    }
}
