package com.novoda.materialised.stories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.materialised.R;
import com.novoda.materialised.hackernews.items.StoryViewModel;
import com.novoda.materialised.hackernews.topstories.StoryView;

import java.util.List;

public final class StoriesAdapter extends RecyclerView.Adapter {
    private final List<StoryViewModel> storyViewModels;

    public StoriesAdapter(List<StoryViewModel> storyViewModels) {
        this.storyViewModels = storyViewModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatable_story_card, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoryView storyView = (StoryView) holder.itemView;
        storyView.updateWith(storyViewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return storyViewModels.size();
    }
}
