package com.novoda.materialised.hackernews.items

import com.novoda.materialised.hackernews.ClickListener

fun convertStoryToViewModel(story: Story, clickListener: ClickListener<StoryViewModel>): StoryViewModel
        = StoryViewModel(story.by, story.kids, story.id, story.score, story.title, story.url, clickListener)

