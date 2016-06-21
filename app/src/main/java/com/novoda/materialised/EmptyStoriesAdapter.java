package com.novoda.materialised;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class EmptyStoriesAdapter extends RecyclerView.Adapter {
    private final int numberOfStories;

    public EmptyStoriesAdapter(int numberOfStories) {
        this.numberOfStories = numberOfStories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatable_story_card, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return numberOfStories;
    }
}
