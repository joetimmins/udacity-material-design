package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.novoda.materialised.hackernews.items.StoryViewModel;
import com.novoda.materialised.hackernews.topstories.TopStoriesView;
import com.novoda.materialised.stories.SingleViewModelTypeAdapter;

import java.util.List;

import org.jetbrains.annotations.NotNull;

final class TopStoriesViewPresenter implements TopStoriesView {

    private final View loadingView;
    private final RecyclerView topStoriesView;

    private SingleViewModelTypeAdapter<StoryViewModel> adapter;

    TopStoriesViewPresenter(View loadingView, RecyclerView topStoriesView) {
        this.loadingView = loadingView;
        this.topStoriesView = topStoriesView;
    }

    @Override
    public void updateWith(@NotNull StoryViewModel storyViewModel) {
        adapter.updateWith(storyViewModel);
    }

    @Override
    public void updateWith(@NotNull final List<StoryViewModel> storyViewModels) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleViewModelTypeAdapter<>(storyViewModels, R.layout.inflatable_story_card);
        topStoriesView.swapAdapter(adapter, false);
    }
}
