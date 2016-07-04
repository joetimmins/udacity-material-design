package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.topstories.database.Story

fun convertStoryToViewModel(story: Story, clickListener: ClickListener<StoryViewModel>): StoryViewModel
        = StoryViewModel(story.by, story.kids, story.id, story.score, story.title, story.url, clickListener)

