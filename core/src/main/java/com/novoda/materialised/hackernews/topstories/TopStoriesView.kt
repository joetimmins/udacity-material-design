package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.ClickListener
import com.novoda.materialised.hackernews.items.StoryViewModel

interface TopStoriesView {
    fun updateWith(initialViewModelList: List<StoryViewModel>)
    fun updateWith(storyViewModel: StoryViewModel, clickListener: ClickListener<StoryViewModel>)
}
