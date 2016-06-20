package com.novoda.materialised.hackernews.topstories;

import com.novoda.materialised.hackernews.items.StoryViewModel;

public interface StoryView {
    void updateWith(StoryViewModel storyViewModel);
}
