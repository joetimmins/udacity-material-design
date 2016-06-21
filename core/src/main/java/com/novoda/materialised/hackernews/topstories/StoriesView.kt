package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.items.StoryViewModel

interface StoriesView {
    fun updateWith(numberOfStories: Int)
    fun updateWith(storyViewModels: List<StoryViewModel>)
}
