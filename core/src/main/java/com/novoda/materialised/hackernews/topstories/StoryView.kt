package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.items.StoryViewModel

interface StoryView {
    fun updateWith(storyViewModel: StoryViewModel)
}
