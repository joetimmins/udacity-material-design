package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;

import com.novoda.materialised.hackernews.topstories.StoryView;

public final class StoryViewHolder extends RecyclerView.ViewHolder {

    private StoryCardView storyView;

    public StoryViewHolder(StoryCardView view) {
        super(view);
        storyView = view;
    }

    public StoryView getStoryView() {
        return storyView;
    }
}
