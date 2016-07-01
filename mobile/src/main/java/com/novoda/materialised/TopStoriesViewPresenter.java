package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.novoda.materialised.hackernews.ClickListener;
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
    public void updateWith(@NotNull StoryViewModel storyViewModel, @NotNull ClickListener<StoryViewModel> clickListener) {
        adapter.updateWith(storyViewModel, clickListener);
    }

    @Override
    public void updateWith(@NotNull final List<StoryViewModel> initialViewModelList) {
        loadingView.setVisibility(View.GONE);
        adapter = new SingleViewModelTypeAdapter<>(initialViewModelList, R.layout.inflatable_story_card);
        topStoriesView.swapAdapter(adapter, false);
    }

}
