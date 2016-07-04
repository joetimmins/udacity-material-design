package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.items.StoryViewModel

interface StoryView {
    fun updateWith(storyViewModel: StoryViewModel)
}
